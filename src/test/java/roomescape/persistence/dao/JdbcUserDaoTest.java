package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.User;

@JdbcTest
class JdbcUserDaoTest {

    private UserDao userDao;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcUserDaoTest(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        userDao = new JdbcUserDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS users CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE users
                  (
                      id   SERIAL,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id)
                  );
                """);
    }

    @DisplayName("데이터베이스에서 유저를 찾는다.")
    @Test
    void find() {
        // given
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('hotteok', 'hoho', 'qwe123', 'USER')");

        // when
        final Optional<User> actual = userDao.find(1L);

        // then
        assertThat(actual).isPresent();
        assertThat(actual.get().getName()).isEqualTo("hotteok");
    }

    @DisplayName("데이터베이스에서 모든 유저를 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('hotteok', 'hoho', 'qwe123', 'USER')");
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('gugu', 'gugu', 'qwe123', 'ADMIN')");

        // when
        final List<User> actual = userDao.findAll();

        // then
        assertThat(actual).extracting("name").containsExactly("hotteok", "gugu");
    }
}
