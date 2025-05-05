package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import roomescape.auth.domain.User;

@JdbcTest
@Import(JdbcUserRepository.class)
@ActiveProfiles("test")
class JdbcUserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcUserRepository jdbcUserRepository;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("TRUNCATE TABLE users");
    }

    private final static RowMapper<User> USER_ROW_MAPPER =
            (rs, rowNum) -> new User(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name")
            );

    @DisplayName("유저를 저장할 수 있다.")
    @Test
    void save() {
        // given
        User user = new User("test@example.com", "password", "멍구");

        // when
        jdbcUserRepository.save(user);

        // then
        List<User> users = jdbcTemplate.query("SELECT email, password, name FROM users", USER_ROW_MAPPER);

        assertThat(users).hasSize(1);
        assertThat(users.getFirst()).isEqualTo(user);
    }

    @DisplayName("유저를 이메일로 조회할 수 있다.")
    @Test
    void findUserByEmail() {
        // given
        String email = "test@example.com";
        String password = "password";
        String name = "멍구";
        jdbcTemplate.update("INSERT INTO users (email, password, name) VALUES (?, ?, ?)",
                email, password, name);

        // when
        Optional<User> result = jdbcUserRepository.findByEmail(email);

        // then
        assertThat(result).isPresent();
        User user = result.get();

        assertAll(
                () -> assertThat(user.getPassword()).isEqualTo(password),
                () -> assertThat(user.getName()).isEqualTo(name)
        );
    }
}
