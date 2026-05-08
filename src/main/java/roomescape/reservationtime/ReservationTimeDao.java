package roomescape.reservationtime;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getObject("start_at", LocalTime.class)
    );
    private final RowMapper<AvailableTime> availableTimeRowMapper = (rs, rowNum) -> new AvailableTime(
            rs.getLong("timeId"),
            rs.getObject("time", LocalTime.class),
            rs.getBoolean("isAvailable")
    );

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    ReservationTime save(LocalTime startAt) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setObject(1, startAt);
            return ps;
        }, keyHolder);

        long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new ReservationTime(generatedId, startAt);
    }

    List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(sql, rowMapper);
    }

    void delete(long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<ReservationTime> findById(long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";

        try {
            ReservationTime time = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<AvailableTime> findAvailableTimes(LocalDate date, Long themeId) {
        String sql =
                "SELECT rt.id AS timeId, rt.start_at AS time, CASE WHEN r.id IS NULL THEN true ELSE false END AS isAvailable "
                        + "FROM reservation_time rt "
                        + "LEFT JOIN reservation AS r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ? "
                        + "ORDER BY rt.id";

        return jdbcTemplate.query(sql, availableTimeRowMapper, date, themeId);
    }
}
