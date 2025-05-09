package roomescape.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.user.domain.Role;
import roomescape.user.domain.User;
import roomescape.user.infrastructure.JwtTokenProvider;
import roomescape.user.infrastructure.fake.FakeUserDao;
import roomescape.user.presentation.dto.LoginRequest;
import roomescape.user.presentation.dto.TokenResponse;
import roomescape.user.presentation.dto.UserResponse;

class AuthServiceTest {

    private final AuthService authService;
    private final FakeUserDao userRepository;

    AuthServiceTest() {
        this.userRepository = new FakeUserDao();
        this.authService = new AuthService(new JwtTokenProvider("secret-key", 360000), userRepository);
    }

    @Test
    @DisplayName("로그인 테스트")
    void login() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new User(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        TokenResponse token = authService.login(loginRequest);

        // then
        assertThat(token.accessToken()).isNotEmpty();
    }

    @Test
    @DisplayName("유저 정보 조회 테스트")
    void getUserTest() {
        // given
        String email = "email@email.com";
        String password = "password";
        userRepository.insert(new User(0L, "name", email, password, Role.USER));
        LoginRequest loginRequest = new LoginRequest(email, password);
        TokenResponse token = authService.login(loginRequest);

        // when
        UserResponse user = authService.getUser(token.accessToken());

        // then
        assertThat(user.name()).isEqualTo("name");
    }

}
