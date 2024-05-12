package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.MemberResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.InitialDataFixture.USER_1;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @DisplayName("로그인 요청을 수행할 수 있다.")
    @Test
    void loginTest() {
        LoginRequest loginRequest = new LoginRequest(USER_1.getEmail().getEmail(), USER_1.getPassword());

        MemberResponse memberResponse = memberService.login(loginRequest);

        assertAll(
                () -> assertThat(memberResponse.name()).isEqualTo(USER_1.getName().getName()),
                () -> assertThat(memberResponse.email()).isEqualTo(USER_1.getEmail().getEmail())
        );
    }

    @DisplayName("존재하지 않는 유저의 정보로 로그인을 요청하는 경우 예외가 발생한다.")
    @Test
    void loginWithNoExistMember() {
        LoginRequest loginRequest = new LoginRequest("no_exist@test.com", "NoExist");

        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비밀번호가 일치하지 않는 요청을 하는 경우 예외가 발생한다.")
    @Test
    void loginWithWrongPasswordMember() {
        LoginRequest loginRequest = new LoginRequest(USER_1.getEmail().getEmail(), "wrongPassword");

        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이메일을 통해 유저를 찾을 수 있다.")
    @Test
    void findMemberByEmailTest() {
        MemberResponse memberResponse = memberService.findMemberByEmail(USER_1.getEmail().getEmail());

        assertThat(memberResponse.email()).isEqualTo(USER_1.getEmail().getEmail());
    }

    @DisplayName("존재하지 않는 이메일로 유저를 찾는 경우 예외가 발생한다.")
    @Test
    void findMemberByInvalidEmailTest() {
        assertThatThrownBy(() -> memberService.findMemberByEmail("wrong@wrong.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ID로 유저를 찾을 수 있다.")
    @Test
    void findById() {
        MemberResponse user1MemberResponse = memberService.findMemberByEmail(USER_1.getEmail().getEmail());

        MemberResponse memberResponse = memberService.findById(user1MemberResponse.id());

        assertThat(memberResponse.name()).isEqualTo(USER_1.getName().getName());
    }

    @DisplayName("유저를 추가할 수 있다")
    @Test
    void createMemberTest() {
        SignupRequest signupRequest = new SignupRequest("create-member-test@test.com", "password", "IAmATest");

        MemberResponse memberResponse = memberService.create(signupRequest);

        assertAll(
                () -> assertThat(memberResponse.email()).isEqualTo(signupRequest.email()),
                () -> assertThat(memberResponse.name()).isEqualTo(signupRequest.name())
        );
    }

    @DisplayName("중복되는 이메일로 회원가입을 할 수 없다.")
    @Test
    void createMemberWithDuplicatedEmail() {
        SignupRequest signupRequest = new SignupRequest(USER_1.getEmail().getEmail(), "password", "IAmATestToo");

        assertThatThrownBy(() -> memberService.create(signupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}