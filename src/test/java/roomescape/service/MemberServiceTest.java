package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
import roomescape.exception.AuthorizationException;
import roomescape.fixture.MemberFixtures;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:test_db_clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("사용자를 추가한다.")
    void createMember() {
        //given
        String name = "abc";
        String email = "test@test.com";
        String password = "1234";
        MemberSignupRequest request = new MemberSignupRequest(name, email, password);

        //when
        MemberResponse result = memberService.add(request);

        //then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getEmail()).isEqualTo(email)
        );
    }

    @Test
    @DisplayName("모든 회원 정보를 조회한다.")
    void findAll() {
        //given
        memberDao.create(MemberFixtures.createUserMember("user1", "user1@test.com", "1234"));
        memberDao.create(MemberFixtures.createUserMember("user2", "user2@test.com", "1234"));

        //when
        List<MemberResponse> result = memberService.findAll();

        //then
        assertAll(
                () -> assertThat(result).hasSize(2)
        );
    }

    @Test
    @DisplayName("이메일이 저장소에 존재하면 예외가 발생한다.")
    void createMemberWithExistEmail() {
        //given
        String email = "test@test.com";
        memberDao.create(MemberFixtures.createUserMember("daon", email, "1432"));
        MemberSignupRequest request = new MemberSignupRequest("abc", email, "1234");

        //when //then
        assertThatThrownBy(() -> memberService.add(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("payload로 로그인 검증 응답을 반환한다.")
    void findAuthInfo() {
        //given
        String email = "test@test.com";
        String name = "daon";
        Member member = memberDao.create(MemberFixtures.createUserMember(name, email, "1234"));

        //when
        Member result = memberService.findAuthInfo(email);

        //then
        assertAll(
                () -> assertThat(member.getId()).isEqualTo(result.getId()),
                () -> assertThat(member.getName().getValue()).isEqualTo(result.getName().getValue()),
                () -> assertThat(member.getEmail().getValue()).isEqualTo(result.getEmail().getValue()),
                () -> assertThat(member.getPassword().getValue()).isEqualTo(result.getPassword().getValue())
        );
    }

    @Test
    @DisplayName("인증 정보가 회원 저장소에 없다면 예외가 발생한다.")
    void findAuthInfoWhenNotExistData() {
        //given
        String email = "test@test.com";

        //when //then
        assertThatThrownBy(() -> memberService.findAuthInfo(email))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("로그인 정보가 존재하지 않으면 예외가 발생한다.")
    void checkLoginInfoWithNotExist() {
        //given
        LoginRequest loginRequest = new LoginRequest("test@test.com", "1111");

        //when //then
        assertThatThrownBy(() -> memberService.checkLoginInfo(loginRequest))
                .isInstanceOf(AuthorizationException.class);
    }
}
