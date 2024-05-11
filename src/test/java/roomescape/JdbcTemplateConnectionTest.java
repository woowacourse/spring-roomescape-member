package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JdbcTemplateConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("JdbcTemplate을 이용하여 DB 연결을 확인한다")
    void jdbcTemplateConnection() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertAll(
                    () -> assertThat(connection).isNotNull(),
                    () -> assertThat(connection.getCatalog()).isEqualTo("DATABASE"),
                    () -> assertThat(connection.getMetaData()
                            .getTables(null, null, "RESERVATION", null).next())
                            .isTrue()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
