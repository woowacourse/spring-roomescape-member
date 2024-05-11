package roomescape.member.service;

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
import roomescape.login.dto.LoginRequest;
import roomescape.login.service.LoginService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/schema.sql", "/initial_test_data.sql"})
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @ParameterizedTest
    @CsvSource({
            "wrongg, admin@example.com",
            "123456, wrong@example.com"
    })
    @DisplayName("이메일 또는 비밀번호가 틀린 경우 예외가 발생한다.")
    void throwExceptionIfIncorrectMemberInfo(String password, String email) {
        LoginRequest loginRequest = new LoginRequest(password, email);

        assertThatThrownBy(() -> loginService.createLoginToken(loginRequest))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("로그인에 성공하면 토큰을 발행한다.")
    void getTokenIfLoginSucceeds() throws AuthenticationException {
        LoginRequest loginRequest = new LoginRequest(MEMBER_4.getPassword().password(), MEMBER_4.getEmail().email());

        String token = loginService.createLoginToken(loginRequest);

        assertThat(token).isNotNull();
    }
}
