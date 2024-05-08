package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.User;
import roomescape.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    void registerTest() {
        User user = userService.register(new UserDto("name", "email@email.com", "password"));

        assertThat(user).isNotNull();
    }

    @Test
    void loginTest() {
        String id = "email@email.com";
        String password = "password";
        userService.register(new UserDto("name", id, password));

        User user = userService.login(id, password);

        assertThat(user.isEmailMatches(id)).isTrue();
        assertThat(user.isPasswordMatches(password)).isTrue();
    }
}
