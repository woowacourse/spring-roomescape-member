package roomescape.reservationtime;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long save(LocalTime startAt) {
        String sql = """
                INSERT INTO reservation_time (start_at)
                VALUES (?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setObject(1, startAt);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<Map<String, Object>> findAll() {
        String sql = """
                SELECT id, start_at
                FROM reservation_time
                """;

        return jdbcTemplate.queryForList(sql);
    }

    public void delete(long id) {
        String sql = """
                DELETE FROM reservation_time
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }

    public Optional<Map<String, Object>> findById(long id) {
        String sql = """
                SELECT id, start_at
                FROM reservation_time
                WHERE id = ?
                """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForMap(sql, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Map<String, Object>> findAvailableTimes(LocalDate date, Long themeId) {
        String sql = """
                SELECT rt.id AS timeId, rt.start_at AS time,
                       CASE WHEN r.id IS NULL THEN true ELSE false END AS isAvailable
                FROM reservation_time rt
                LEFT JOIN reservation AS r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ?
                ORDER BY rt.id
                """;

        return jdbcTemplate.queryForList(sql, date, themeId);
    }
}
