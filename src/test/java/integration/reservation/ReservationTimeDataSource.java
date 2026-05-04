package integration.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;

@TestComponent
public class ReservationTimeDataSource {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clearTable() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    public void clearId() {
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }
}
