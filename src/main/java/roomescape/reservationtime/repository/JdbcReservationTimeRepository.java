package roomescape.reservationtime.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeDuplicatedException;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> reservationTimeRowMapper = (rs, rowNum) ->
            ReservationTime.of(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new ReservationTimeDuplicatedException(reservationTime.getStartAt());
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return ReservationTime.of(id, reservationTime.getStartAt());
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        List<ReservationTime> result = jdbcTemplate.query(sql, reservationTimeRowMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time ORDER BY id";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    @Override
    public List<ReservationTime> findAvailableTimesByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT rt.id, rt.start_at
                FROM reservation_time rt
                WHERE rt.id NOT IN (
                    SELECT r.time_id
                    FROM reservation r
                    WHERE r.date = ?
                      AND r.theme_id = ?
                )
                ORDER BY rt.id
                """;

        return jdbcTemplate.query(sql, reservationTimeRowMapper, date, themeId);
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
