package roomescape.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.dto.TokenResponse;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.exception.UserNotFoundException;
import roomescape.domain.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtManager jwtManager;

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtProperties jwtProperties;

    @BeforeEach
    void init() {
        lenient().when(jwtProperties.getCookieKey())
                .thenReturn("token");
    }

    @Nested
    class login {
        @DisplayName("로그인을 한다.")
        @Test
        void loginTest1() {
            final String email = "zxczxc@naver.com";
            final String password = "1234";
            final User user = new User(1L, new Name("꾹"), email, password, Roles.USER);
            final String token = "token";
            final TokenResponse expect = new TokenResponse(token);
            final LoginRequest loginRequest = new LoginRequest(email, password);

            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(jwtManager.createToken(any())).willReturn("token");

            // when
            final TokenResponse result = authService.login(loginRequest);

            // then
            assertThat(result).isEqualTo(expect);
        }

        @DisplayName("비밀번호가 다르다면 InvalidAuthorizationException 예외를 반환한다.")
        @Test
        void login_throwsException() {
            // given
            final String email = "zxczxc@naver.com";
            final String password = "1234";
            final User userWithoutId = new User(1L, new Name("꾹"), email, password, Roles.USER);

            given(userRepository.findByEmail(email)).willReturn(Optional.of(userWithoutId));

            final LoginRequest loginRequest = new LoginRequest(email, "123");

            // when & then
            assertThatThrownBy(() -> authService.login(loginRequest)).isInstanceOf(InvalidAuthorizationException.class);
        }

        @DisplayName("존재하지 않는 이메일이라면 UserNotFoundException 예외를 반환한다.")
        @Test
        void login_throwsException2() {
            // given
            final String email = "sdasdsad@naver.com";
            given(userRepository.findByEmail(email)).willReturn(Optional.empty());
            final LoginRequest loginRequest = new LoginRequest(email, "123");

            // when & then
            assertThatThrownBy(() -> authService.login(loginRequest)).isInstanceOf(UserNotFoundException.class);
        }
    }
}
