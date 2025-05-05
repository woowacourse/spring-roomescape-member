package roomescape.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static roomescape.auth.exception.AuthErrorCode.INVALID_PASSWORD;
import static roomescape.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static roomescape.auth.exception.AuthErrorCode.USER_NOT_FOUND;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.domain.User;
import roomescape.auth.domain.UserRepository;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.dto.UserResponse;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("이메일과 비밀번호로 토큰을 생성할 수 있다")
    void createToken_success() {
        TokenRequest request = new TokenRequest("email@test.com", "1234");
        User user = new User("email@test.com", "1234", "멍구");

        given(userRepository.findByEmail("email@test.com")).willReturn(Optional.of(user));
        given(jwtTokenProvider.createToken("email@test.com")).willReturn("token-value");

        TokenResponse response = authService.createToken(request);

        assertThat(response.accessToken()).isEqualTo("token-value");
    }

    @Test
    @DisplayName("유효한 토큰으로 사용자를 조회할 수 있다")
    void findUserByToken_success() {
        String token = "valid-token";
        String email = "email@test.com";
        User user = new User( email, "pass", "멍구");

        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        given(jwtTokenProvider.getPayload(token)).willReturn(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        UserResponse response = authService.findUserByToken(token);

        assertThat(response.name()).isEqualTo("멍구");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void createToken_wrongPassword() {
        TokenRequest request = new TokenRequest("email@test.com", "wrong");
        User user = new User("email@test.com", "password", "멍구");

        given(userRepository.findByEmail("email@test.com")).willReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage(INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 예외를 발생시킨다")
    void findUserByToken_invalidToken() {
        String token = "invalid-token";

        given(jwtTokenProvider.validateToken(token)).willReturn(false);

        assertThatThrownBy(() -> authService.findUserByToken(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }

    @DisplayName("존재하지 않는 이메일로 로그인 시도하면 예외를 발생시킨다.")
    @Test
    void findUserByToken_emailNotFound() {
        // given
        TokenRequest tokenRequest = new TokenRequest("notfound@email.com", "password");
        given(userRepository.findByEmail(tokenRequest.email())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageContaining(USER_NOT_FOUND.getMessage());
    }
}
