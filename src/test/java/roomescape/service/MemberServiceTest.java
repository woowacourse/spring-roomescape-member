package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialMemberFixture.MEMBER_4;

import javax.naming.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberNameResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/schema.sql", "/initial_test_data.sql"})
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @ParameterizedTest
    @CsvSource({
            "wrongg, admin@example.com",
            "123456, wrong@example.com"
    })
    @DisplayName("이메일 또는 비밀번호가 틀린 경우 예외가 발생한다.")
    void throwExceptionIfIncorrectMemberInfo(String password, String email) {
        LoginRequest loginRequest = new LoginRequest(password, email);

        assertThatThrownBy(() -> memberService.createLoginToken(loginRequest))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("로그인에 성공하면 토큰을 발행한다.")
    void getTokenIfLoginSucceeds() throws AuthenticationException {
        LoginRequest loginRequest = new LoginRequest(MEMBER_4.getPassword().password(), MEMBER_4.getEmail().email());

        String token = memberService.createLoginToken(loginRequest);

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 형식의 토큰으로 로그인 시도 시 예외가 발생한다.")
    void throwExceptionIfInvalidTokenFormat() {
        assertThatThrownBy(() -> memberService.getMemberResponse("invalid token"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("토큰에 대응하는 멤버 정보를 가져온다.")
    void getLoginMember() throws AuthenticationException {
        LoginRequest loginRequest = new LoginRequest(MEMBER_4.getPassword().password(), MEMBER_4.getEmail().email());
        String token = memberService.createLoginToken(loginRequest);

        MemberNameResponse memberNameResponse = memberService.getMemberResponse(token);

        assertThat(memberNameResponse.name()).isEqualTo(MEMBER_4.getName().name());
    }
}
