package integration.reservation;

import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;

@TestComponent
public class ReservationDataSource {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clearTable() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    public void clearId() {
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }

    public void insertReservationTime(LocalTime reservationTime) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTime);
    }

    public boolean hasReservationById(Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }
}
