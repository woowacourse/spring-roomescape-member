package roomescape.domain.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.login.domain.User;
import roomescape.domain.login.repository.UserRepository;
import roomescape.exception.ClientIllegalArgumentException;

class LoginServiceTest {

    private LoginService loginService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        loginService = new LoginService(userRepository);
        userRepository.insert(new User(null, "admin", "admin@gmail.com", "123456"));
    }

    @DisplayName("원하는 id의 유저를 찾을 수 있습니다.")
    @Test
    void should_find_user_by_id() {
        User expectedUser = new User(1L, "admin", "admin@gmail.com", "123456");

        User actualUser = userRepository.findById(1L).get();

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @DisplayName("없는 id의 유저를 찾으면 예외가 발생합니다.")
    @Test
    void should_throw_exception_when_find_by_non_exist_id() {
        assertThatThrownBy(() -> loginService.findUserById(2L))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("없는 user를 조회 했습니다.");
    }
}
