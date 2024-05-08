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

    private static final User ADMIN_USER = new User(1L, "admin", "admin@gmail.com", "123456");

    private LoginService loginService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        loginService = new LoginService(userRepository);
        userRepository.insert(ADMIN_USER);
    }

    @DisplayName("원하는 id의 유저를 찾을 수 있습니다.")
    @Test
    void should_find_user_by_id() {
        User expectedUser = ADMIN_USER;

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

    @DisplayName("email과 password로 user를 찾을 수 있습니다.")
    @Test
    void should_find_user_by_email_and_password() {
        User expectedUser = ADMIN_USER;

        User actualUser = loginService.findUserByEmailAndPassword("admin@gmail.com", "123456");

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @DisplayName("존재 하지 않는 email로 user를 찾으려 하면, 예외가 발생합니다.")
    @Test
    void should_throw_exception_when_find_user_by_non_exist_email() {
        assertThatThrownBy(() -> loginService.findUserByEmailAndPassword("wrongEmail@gmail.com", "123456"))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("이메일 또는 비밀번호를 잘못 입력했습니다.");
    }

    @DisplayName("틀린 password로 user를 찾으려 하면, 예외가 발생합니다.")
    @Test
    void should_throw_exception_when_find_user_by_wrong_password() {
        assertThatThrownBy(() -> loginService.findUserByEmailAndPassword("admin@gmail.com", "1234567"))
                .isInstanceOf(ClientIllegalArgumentException.class)
                .hasMessage("이메일 또는 비밀번호를 잘못 입력했습니다.");
    }
}
