package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DbConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 데이터 베이스 연결 여부를 확인한다")
    @Test
    void reservationDBConnectionTest() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("예약 시간 데이터 베이스 연결 여부를 확인한다")
    @Test
    void reservationTimeDBConnectionTest() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION_TIME", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("방 테마 데이터 베이스 연결 여부를 확인한다")
    @Test
    void themeDBConnectionTest() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "THEME", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
