package roomescape.learning;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.common.CleanUp;

@JdbcTest
@Import(CleanUp.class)
class DataTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.all();
    }

    @Test
    void 데이터베이스_연결을_검증한다() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            boolean hasReservationTable = connection.getMetaData()
                    .getTables(null, null, "RESERVATION", null)
                    .next();
            assertThat(hasReservationTable).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 방탈출_예약_목록을_조회한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", "2025-08-05", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "코니", "2025-08-05", 1, 1);

        Integer reservationCount = jdbcTemplate.queryForObject("SELECT count(1) FROM reservation", Integer.class);
        assertThat(reservationCount).isEqualTo(2);
    }

    @Test
    void 방탈출_예약_생성_조회_삭제_테스트() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", "2025-08-05", 1, 1);

        Integer createdCount = jdbcTemplate.queryForObject("SELECT count(1) FROM reservation", Integer.class);
        assertThat(createdCount).isEqualTo(1);

        int deleted = jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", 1L);
        assertThat(deleted).isEqualTo(1);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) FROM reservation", Integer.class);
        assertThat(countAfterDelete).isZero();
    }
}