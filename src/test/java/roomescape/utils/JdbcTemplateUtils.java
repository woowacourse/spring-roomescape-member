package roomescape.utils;

import java.time.LocalTime;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public final class JdbcTemplateUtils {

    private JdbcTemplateUtils() {
    }

    public static void deleteAllTables(final JdbcTemplate jdbcTemplate) {
        try {
            jdbcTemplate.update("delete from reservation");
            jdbcTemplate.update("delete from reservation_time");
            jdbcTemplate.update("delete from theme");
            jdbcTemplate.update("delete from users");
        } catch (final DataAccessException e) {
            throw new RuntimeException("테이블 삭제 중 오류 발생", e);
        }
    }

    public static void insertReservationTime(final JdbcTemplate jdbcTemplate, final Long id, final LocalTime time) {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", id, time);
    }

    public static void insertTheme(final JdbcTemplate jdbcTemplate, final Long id, final String name, final String desc,
                                   final String thumbnail) {
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail) VALUES (?, ?, ?, ?)", id, name, desc,
                thumbnail);
    }

    public static void insertUser(final JdbcTemplate jdbcTemplate, final Long id, final String name, final String email,
                                  final String password, final String role) {
        jdbcTemplate.update("INSERT INTO users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?)", id, name,
                email, password, role);
    }

    public static void insertReservation(final JdbcTemplate jdbcTemplate, final Long id, final Long userId,
                                         final String date, final Long timeId, final Long themeId) {
        jdbcTemplate.update("INSERT INTO reservation (id, user_id, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)", id,
                userId, date, timeId, themeId);
    }
}
