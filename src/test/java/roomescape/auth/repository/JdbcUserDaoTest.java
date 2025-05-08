package roomescape.auth.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.auth.domain.User;
import roomescape.util.repository.TestDataSourceFactory;

class JdbcUserDaoTest {

    private JdbcUserDao jdbcUserDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcUserDao = new JdbcUserDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("이메일에 해당하는 사용자를 조회한다")
    @Test
    void find_by_email_test() {
        // given
        String email = "rookie@woowa.com";

        // when
        User findUser = jdbcUserDao.findByEmail(email).get();

        // then
        assertAll(
                () -> assertThat(findUser.getId()).isEqualTo(1L),
                () -> assertThat(findUser.getEmail()).isEqualTo("rookie@woowa.com"),
                () -> assertThat(findUser.getName()).isEqualTo("루키")
        );
    }

}
