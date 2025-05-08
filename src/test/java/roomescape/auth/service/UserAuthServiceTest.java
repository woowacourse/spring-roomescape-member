package roomescape.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.entity.User;
import roomescape.auth.repository.FakeUserRepository;
import roomescape.auth.repository.UserRepository;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.SignupRequest;
import roomescape.exception.conflict.ConflictException;
import roomescape.exception.unauthorized.UserUnauthorizedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserAuthServiceTest {
    private final UserRepository userRepository = new FakeUserRepository();
    private final TokenProvider tokenProvider = new FakeJwtTokenProvider();
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

    @DisplayName("중복되는 이메일의 유저는 생성할 수 없다")
    @Test
    void duplicateEmailSignupException() {
        // given
        String email = "test@example.com";
        String password = "1234";
        String name = "test";
        userRepository.save(new User(1L, name, email, password));

        SignupRequest request = new SignupRequest(
                email,
                password,
                name
        );

        // when & then
        assertThatThrownBy(() -> {
            service.signup(request);
        }).isInstanceOf(ConflictException.class);
    }
}
