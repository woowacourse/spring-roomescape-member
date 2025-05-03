package roomescape.test_util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class JdbcTestUtil {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTestUtil(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insertReservation(final String name, final LocalDate date, final long timeId, final long themeId) {
        final String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setDate(2, Date.valueOf(date));
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public long insertReservationTime(final LocalTime time) {
        final String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setTime(1, Time.valueOf(time));
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public long insertTheme(final String name) {
        final String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, "");
            ps.setString(3, "");
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int countReservation() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation WHERE id IS NOT NULL", Integer.class);
        return Objects.requireNonNull(count);
    }

    public int countReservationTime() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time WHERE id IS NOT NULL", Integer.class);
        return Objects.requireNonNull(count);
    }

    public int countTheme() {
        final Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme WHERE id IS NOT NULL", Integer.class);
        return Objects.requireNonNull(count);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM reservation WHERE id IS NOT NULL");
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id IS NOT NULL");
        jdbcTemplate.update("DELETE FROM theme WHERE id IS NOT NULL");
    }
}
