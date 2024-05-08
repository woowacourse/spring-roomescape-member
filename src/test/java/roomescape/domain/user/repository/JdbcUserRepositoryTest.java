package roomescape.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.user.User;
import roomescape.fixture.UserFixture;

@JdbcTest
class JdbcUserRepositoryTest {
    private final UserRepository userRepository;

    @Autowired
    JdbcUserRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.userRepository = new JdbcUserRepository(jdbcTemplate);
    }

    @Test
    void 유저를_저장한다() {
        User user = UserFixture.user("abc.gmail.com");

        User savedUser = userRepository.save(user);

        assertAll(
                () -> assertThat(savedUser.getName()).isEqualTo(user.getName()),
                () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail()),
                () -> assertThat(savedUser.getPassword()).isEqualTo(user.getPassword())
        );
    }
}
