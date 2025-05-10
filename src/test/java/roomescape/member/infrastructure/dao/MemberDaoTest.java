package roomescape.member.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@JdbcTest
class MemberDaoTest {

    private final MemberDao memberDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    MemberDaoTest(JdbcTemplate jdbcTemplate) {
        this.memberDao = new MemberDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("이메일을 사용한 사용자 조회 테스트")
    void findByEmailTest() {
        // given
        String email = "email@email.com";
        String initQuery = "INSERT INTO member (name, email, password, role) "
                + "VALUES ('name', 'email@email.com', 'password', 'USER')";
        jdbcTemplate.update(initQuery);

        // when
        Optional<Member> member = memberDao.findByEmail(email);

        // then
        assertThat(member.get().getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("아이디 사용한 사용자 조회 테스트")
    void findByIdTest() {
        // given
        String initQuery = "INSERT INTO member (name, email, password, role) "
                + "VALUES ('name', 'email@email.com', 'password', 'USER')";
        jdbcTemplate.update(initQuery);

        // when
        Optional<Member> member = memberDao.findById(1L);

        // then
        assertThat(member.get().getName()).isEqualTo("name");
    }

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    void findAll() {
        // given
        String initQuery = "INSERT INTO member (name, email, password, role) "
                + "VALUES "
                + "('name1', 'email1@email.com', 'password', 'USER'),"
                + "('name2', 'email2@email.com', 'password', 'USER'),"
                + "('name3', 'email3@email.com', 'password', 'USER');";
        jdbcTemplate.update(initQuery);

        // when
        List<GetMemberResponse> members = memberDao.findAll();

        // then
        assertThat(members).hasSize(3);
    }

    @Test
    @DisplayName("사용자 추가 테스트")
    void insertTest() {
        // given
        CreateMemberRequest createMemberRequest = new CreateMemberRequest("name", "email@email.com", "password",
                Role.USER);

        // when
        Member member = memberDao.insert(createMemberRequest);

        // then
        assertThat(member).isNotNull();
    }
}
