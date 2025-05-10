package roomescape.service.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.auth.LoginRequest;
import roomescape.exceptions.auth.AuthorizationException;
import roomescape.infrastructure.jwt.JwtTokenProvider;

@SpringBootTest
@Transactional
@Sql({"/fixtures/schema-test.sql", "/fixtures/member.sql"})
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("이메일과 비밀번호가 같은 Member가 있을 경우, 토큰값을 반환한다.")
    void login() {
        //given
        String email = "user1@example.com";
        String password = "user123";
        LoginRequest request = new LoginRequest(email, password);
        // when
        String token = authenticationService.login(request);
        // then
        String actual = jwtTokenProvider.resolveToken(token).email();
        assertThat(actual).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일과 비밀번호가 틀릴 경우, 예외가 발생한다.")
    void login_ShouldThrowExceptionWhenWrongRequest() {
        //given
        String email = "wrongUserInfo@example.com";
        String password = "user123";
        LoginRequest request = new LoginRequest(email, password);
        // when & then
        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageContaining("[ERROR] 회원 정보를 찾을 수 없습니다.");
    }
}
