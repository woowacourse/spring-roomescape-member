package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.User;
import roomescape.domain.repository.UserRepository;
import roomescape.exception.user.AuthenticationFailureException;
import roomescape.service.security.JwtUtils;
import roomescape.web.dto.request.LoginRequest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일과 비밀번호로 로그인 기능을 제공한다")
    void login_ShouldProvideLoginFeature() {
        // given
        User user = new User("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "password");
        User savedUser = userRepository.save(user);

        // when
        String token = userService.login(request);

        // then
        Assertions.assertThat(JwtUtils.decode(token)).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("이메일이 없는 정보라면 로그인 중 예외를 발생시킨다")
    void login_ShouldFailed_WhenEmailDoesNotExist() {
        // given
        LoginRequest request = new LoginRequest("hello", "password");

        // when & then
        Assertions.assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인 중 예외를 발생시킨다")
    void login_ShouldFailed_WhenInvalidLoginInfo() {
        // given
        User user = new User("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "world");
        userRepository.save(user);

        // when & then
        Assertions.assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(AuthenticationFailureException.class);
    }

    @Test
    @DisplayName("유효한 토큰인지 확인할 수 있다")
    void validateToken_ShouldVerifyToken() {
        // given
        User user = new User("name", "hello", "password");
        LoginRequest request = new LoginRequest("hello", "password");
        userRepository.save(user);
        String token = userService.login(request);

        // when & then
        Assertions.assertThatCode(() -> userService.findUserByToken(token))
                .doesNotThrowAnyException();

    }

    @Test
    @DisplayName("유효하지 않는 토큰인지 확인할 수 있다")
    void validateToken_ShouldThrowException_WhenTokenIsNotValid() {
        Assertions.assertThatCode(() -> userService.findUserByToken("hello, world"))
                .isInstanceOf(AuthenticationFailureException.class);
    }
}
