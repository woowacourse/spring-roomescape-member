package roomescape.dao;

import org.junit.jupiter.api.Test;
import roomescape.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginDaoTest {

    private final LoginDao loginDao = new LoginDao();

    @Test
    void insertTest() {
        String name = "name";
        String email = "email@email.com";
        String password = "password";

        User user = loginDao.insert(name, email, password);

        assertThat(user).isNotNull();
    }

    @Test
    void findByEmailTest() {
        loginDao.insert("name", "email@email.com", "password");

        Optional<User> user = loginDao.findByEmail("email@email.com");

        assertThat(user.isPresent()).isTrue();
    }
}
