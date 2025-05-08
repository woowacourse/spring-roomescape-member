package roomescape.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.repository.FakeUserRepository;
import roomescape.auth.repository.UserRepository;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.exception.unauthorized.UserUnauthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserAuthServiceTest {
    private final UserRepository userRepository = new FakeUserRepository();
    private final TokenProvider tokenProvider = new FakeTokenProvider();
    private final UserAuthService service = new UserAuthService(userRepository, tokenProvider);

    @DisplayName("존재하지 않는 유저가 로그인을 요청하는 경우 예외가 발생한다.")
    @Test
    void notExistUserLoginException() {
        // given
        LoginRequest request = new LoginRequest("test@example.com", "1234");

        // when & then
        assertThatThrownBy(() -> {
            service.login(request);
        }).isInstanceOf(UserUnauthorizedException.class);
    }
}
