package roomescape.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.dto.LoginRequest;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.auth.dto.TokenResponse;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.exception.UserNotFoundException;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.utils.PasswordFixture;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    private String cookieKey;

    @BeforeEach
    void init() {
        cookieKey = jwtProperties.getCookieKey();
    }

    @DisplayName("유저 정보 조회")
    @Nested
    class userInfo {
        @DisplayName("토큰을 검증하여 유저 정보를 반환한다.")
        @Test
        void userInfoTest() {
            // given
            final Name name = new Name("꾹");
            final String email = "asdad@naver.com";

            final Password password = PasswordFixture.generate();
            final User savedUser = userRepository.save(User.withoutId(name, email, password, Roles.USER));
            final UserInfoResponse expected = UserInfoResponse.from(savedUser);
            final LoginUserDto loginUserDto = new LoginUserDto(savedUser.getId(), savedUser.getRole());

            // when
            final UserInfoResponse result = authService.getUserInfo(loginUserDto);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @DisplayName("존재하지 않는 id의 경우 예외를 반환한다.")
        @Test
        void userInfo_throwsException() {
            // given
            final LoginUserDto loginUserDto = new LoginUserDto(210L, Roles.USER);

            // when & then
            assertThatThrownBy(() -> authService.getUserInfo(loginUserDto)).isInstanceOf(UserNotFoundException.class);
        }
    }

    @DisplayName("로그인 정보 조회")
    @Nested
    class loginInfo {
        @DisplayName("토큰을 검증하여 유저 정보를 반환한다.")
        @Test
        void loginUserByTokenTest1() {
            // given
            final Name name = new Name("꾹");
            final String email = "asdad@naver.com";

            final Password password = PasswordFixture.generate();
            final User savedUser = userRepository.save(User.withoutId(name, email, password, Roles.USER));
            final String token = jwtManager.createToken(savedUser);
            final LoginUserDto expected = LoginUserDto.from(savedUser);

            // when
            final LoginUserDto result = authService.getLoginUser(token);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @DisplayName("잘못된 토큰이라면 InvalidAuthorizationException을 반환한다.")
        @Test
        void loginUser_throwsException() {
            // given
            final String invalidToken = "213123213214sdadcxzcxz";

            // when & then
            assertThatThrownBy(() -> authService.getLoginUser(invalidToken)).isInstanceOf(
                    InvalidAuthorizationException.class);
        }

        @DisplayName("쿠키를 통해 로그인 정보를 검증한다.")
        @Test
        void loginUserByCookieTest() {
            // given
            final Name name = new Name("쿠키유저");
            final String email = "cookie@naver.com";
            final Password password = PasswordFixture.generate();
            final User savedUser = userRepository.save(User.withoutId(name, email, password, Roles.USER));
            final String token = jwtManager.createToken(savedUser);
            final Cookie[] cookies = {new Cookie(cookieKey, token)};
            final LoginUserDto expected = LoginUserDto.from(savedUser);

            // when
            final LoginUserDto result = authService.getLoginUser(cookies);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @DisplayName("쿠키에 토큰이 없으면 예외를 반환한다.")
        @Test
        void loginUserByCookie_noToken_throwsException() {
            // given
            final Cookie[] cookies = {new Cookie(cookieKey, "")};

            // when & then
            assertThatThrownBy(() -> authService.getLoginUser(cookies)).isInstanceOf(
                    InvalidAuthorizationException.class);
        }
    }

    @DisplayName("로그인")
    @Nested
    class login {
        @DisplayName("이메일과 비밀번호가 일치하면 로그인이 성공한다.")
        @Test
        void login_success() {
            // given
            final Name name = new Name("로그인유저");
            final String email = "login@test.com";
            final Password password = Password.encrypt("password123", passwordEncryptor);
            userRepository.save(User.withoutId(name, email, password, Roles.USER));
            final LoginRequest loginRequest = new LoginRequest(email, "password123");

            // when
            final TokenResponse result = authService.login(loginRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.token()).isNotBlank();
        }

        @DisplayName("비밀번호가 다르면 예외가 발생한다.")
        @Test
        void login_wrongPassword() {
            // given
            final Name name = new Name("로그인유저");
            final String email = "login@test.com";
            final Password password = Password.encrypt("password123", passwordEncryptor);
            userRepository.save(User.withoutId(name, email, password, Roles.USER));
            final LoginRequest loginRequest = new LoginRequest(email, "wrongpassword");

            // when & then
            assertThatThrownBy(() -> authService.login(loginRequest)).isInstanceOf(InvalidAuthorizationException.class);
        }

        @DisplayName("존재하지 않는 이메일이면 예외가 발생한다.")
        @Test
        void login_userNotFound() {
            // given
            final LoginRequest loginRequest = new LoginRequest("notfound@test.com", "password123");

            // when & then
            assertThatThrownBy(() -> authService.login(loginRequest)).isInstanceOf(UserNotFoundException.class);
        }
    }
}
