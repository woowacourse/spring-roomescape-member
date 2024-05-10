package roomescape.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.user.User;

@JdbcTest
class JdbcUserRepositoryTest {
    private final UserRepository userRepository;

    @Autowired
    JdbcUserRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.userRepository = new JdbcUserRepository(jdbcTemplate);
    }

    @Test
    void 이메일로_유저를_찾는다() {
        User user = userRepository.findByEmail("prin@gmail.com").orElseThrow();

        assertThat(user.getName()).isEqualTo("프린");
    }
}
