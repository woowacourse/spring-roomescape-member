package roomescape.reservationTime;

import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

@TestConfiguration
public class ReservationTimeTestDataConfig {

    public static final LocalTime DEFAULT_DUMMY_TIME = LocalTime.of(2, 40);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Long defaultDummyTimeId;

    @PostConstruct
    public void setUpTestData() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", DEFAULT_DUMMY_TIME.toString());

        defaultDummyTimeId = jdbcTemplate.queryForObject(
            "SELECT id FROM reservation_time WHERE start_at = ?", Long.class, DEFAULT_DUMMY_TIME.toString());
    }

    public Long getDefaultDummyTimeId() {
        return defaultDummyTimeId;
    }
}
