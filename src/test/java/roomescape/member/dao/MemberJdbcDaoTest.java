package roomescape.member.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRegistrationInfo;

@JdbcTest
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class MemberJdbcDaoTest {

    private final MemberJdbcDao memberDao;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MemberJdbcDaoTest(DataSource dataSource) {
        this.memberDao = new MemberJdbcDao(dataSource);
    }

    @Test
    @DisplayName("완전한 회원정보를 정상적으로 저장한다.")
    void save_ShouldRegisterSignUpData_WhenMemberRegisterInfoGiven() {
        // Given
        Member member = new Member("new@memeber.com", "password", "newbie");

        // When
        Member registeredMember = memberDao.save(member);

        // Then
        assertAll(() -> {
            assertNotEquals(0, registeredMember.getId());
            assertEquals("new@memeber.com", registeredMember.getEmail());
            assertEquals("newbie", registeredMember.getName());
            assertEquals("password", registeredMember.getPassword());
        });
    }

    @Test
    @DisplayName("데이터 베이스에 저장되어있는 모든 회원 정보를 반환한다.")
    void findAll_ShouldReturnAllRegistration_WhenCalled() {
        // Given
        List<Member> expectedMembers = List.of(
                new Member(1, "kimdobby@wootaeco.com", "pass1", "도비"),
                new Member(2, "pke@best.com", "pass2", "피케이"),
                new Member(3, "admin@admin.com", "1234", "어드민"),
                new Member(4, "test@test.com", "test", "테스트")
        );

        // When
        List<Member> members = memberDao.findAll();

        // Then
        assertEquals(expectedMembers, members);
    }

    @Test
    @DisplayName("회원 가입 정보를 이메일로 검색하여 반환한다.")
    void findRegistrationInfoByEmail_ShouldReturnRegistration_WhenFindByEmail() {
        // Given
        String email = "test@test.com";

        // When
        MemberRegistrationInfo registrationInfo = memberDao.findRegistrationInfoByEmail(email);

        // Then
        assertAll(() -> {
            assertNotNull(registrationInfo);
            assertEquals("테스트", registrationInfo.name());
            assertEquals("test@test.com", registrationInfo.email());
            assertEquals("test", registrationInfo.password());
        });
    }

    @Test
    @DisplayName("존재하는 특정 id를 가진 회원 정보를 정상적으로 지운다.")
    void delete_ShouldDeleteMemberData_WhenCalledById() {
        // Given & When
        memberDao.delete(1);

        // Then
        assertEquals(3, memberDao.findAll().size());
    }

}
