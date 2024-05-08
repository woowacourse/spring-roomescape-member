package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.User;
import roomescape.dto.LoginDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginServiceTest {

    @Autowired
    private final LoginService loginService;

    @Autowired
    public LoginServiceTest(LoginService loginService) {
        this.loginService = loginService;
    }

    @Test
    void addUserTest() {
        User user = loginService.addUser(new LoginDto("name", "email@email.com", "password"));

        assertThat(user).isNotNull();
    }

    @Test
    void loginTest() {
        String id = "email@email.com";
        String password = "password";
        loginService.addUser(new LoginDto("name", id, password));

        User user = loginService.login(id, password);

        assertThat(user.isEmailMatches(id)).isTrue();
        assertThat(user.isPasswordMatches(password)).isTrue();
    }
}
