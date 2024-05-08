package roomescape.dao;

import org.junit.jupiter.api.Test;
import roomescape.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    private final UserDao userDao = new UserDao();

    @Test
    void insertTest() {
        String name = "name";
        String email = "email@email.com";
        String password = "password";

        User user = userDao.insert(name, email, password);

        assertThat(user).isNotNull();
    }

    @Test
    void findByEmailTest() {
        userDao.insert("name", "email@email.com", "password");

        Optional<User> user = userDao.findByEmail("email@email.com");

        assertThat(user.isPresent()).isTrue();
    }
}
