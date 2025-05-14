package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class JdbcTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("Spring JDBC 연결 테스트")
    @Test
    void test1() {
        try (final Connection connection = jdbcTemplate.getDataSource()
                .getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("TEST-DATABASE");
            assertThat(connection.getMetaData()
                    .getTables(null, null, "RESERVATION", null)
                    .next()).isTrue();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
