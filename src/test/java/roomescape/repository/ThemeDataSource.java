package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.domain.ReservationStatus;

@Component
public class ThemeDataSource {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clearTable() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    public void clearId() {
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
    }

    public void insertReservedReservation(String name, LocalDate date, Long themeId, Long timeId) {
        jdbcTemplate.update("INSERT INTO reservation (name, date, theme_id, time_id, status) VALUES (?, ?, ?, ?, ?)",
                name, date,
                themeId, timeId, ReservationStatus.RESERVED.toString());
    }

    public void insertThemesByCount(int count) {
        for (int i = 0; i < count; i++) {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_image_url) VALUES (?, ?, ?)", "테마" + i,
                    "설명" + i, "https://image.com/image" + i + ".png");
        }
    }

    public void insertTimeByStartToEndWithOneHourRotation(int startHour, int endHour) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        for (int i = startHour; i <= endHour; i++) {
            jdbcTemplate.update(sql, LocalTime.of(i, 0));
        }
    }

    public void insertReservedReservationByTheme(long themeId, int reservationCount) {
        for (long timeId = 1L; timeId <= reservationCount; timeId++) {
            jdbcTemplate.update(
                    "INSERT INTO reservation (name, date, theme_id, time_id, status) VALUES (?, ?, ?, ?, ?)",
                    "바니", LocalDate.now(), themeId, timeId, ReservationStatus.RESERVED.toString());
        }
    }
}
