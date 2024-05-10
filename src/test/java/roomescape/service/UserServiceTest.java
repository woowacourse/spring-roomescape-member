package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.controller.request.UserLoginRequest;
import roomescape.exception.AuthorizationException;
import roomescape.model.User;
import roomescape.service.fake.FakeUserDao;

class UserServiceTest {

    private final FakeUserDao userDao = new FakeUserDao();
    private final UserService userService = new UserService(userDao);

    @BeforeEach
    void setUp() {
        userDao.clear();
    }

    @DisplayName("아이디와 비밀번호가 같은 유저가 존재하면 해당 유저를 반환한다.")
    @Test
    void should_find_user_when_user_exist() {
        User user = new User(1L, "배키", "hello@email.com", "1234");
        userDao.addUser(user);
        UserLoginRequest request = new UserLoginRequest("1234", "hello@email.com");

        User finduser = userService.findUserByEmailAndPassword(request);

        assertThat(finduser).isEqualTo(user);
    }

    @DisplayName("아이디와 비밀번호 같은 유저가 없으면 예외가 발생한다.")
    @Test
    void should_not_find_user_when_user_not_exist() {
        User user = new User(1L, "배키", "hello@email.com", "1234");
        userDao.addUser(user);
        UserLoginRequest request = new UserLoginRequest("1111", "sun@email.com");

        assertThatThrownBy(() -> userService.findUserByEmailAndPassword(request))
                .isInstanceOf(AuthorizationException.class);
    }

}
