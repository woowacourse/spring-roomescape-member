package roomescape.repository.reservation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeAvailability;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );


    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTime save(ReservationTime time) {
        String sql = "INSERT INTO reservation_time (start_at) " +
                "VALUES (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(time.getStartAt()));
            return ps;
        }, keyHolder);
        return new ReservationTime(keyHolder.getKey().longValue(), time.getStartAt());
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT id, start_at " +
                "FROM reservation_time " +
                "WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<ReservationTime> findByStartAt(LocalTime startAt) {
        String sql = "SELECT id, start_at " +
                "FROM reservation_time " +
                "WHERE start_at = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, Time.valueOf(startAt)));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at " +
                "FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public List<ReservationTimeAvailability> findReservationTimeAvailabilities(LocalDate date, Long themeId) {
        String sql = "SELECT rt.id, rt.start_at, " +
                "CASE WHEN r.id IS NULL " +
                "THEN FALSE ELSE TRUE END AS already_booked " +
                "FROM reservation_time AS rt " +
                "LEFT JOIN (" +
                "SELECT id, reservation_time_id " +
                "FROM reservation " +
                "WHERE date = ? AND theme_id = ?) AS r " +
                "ON rt.id = r.reservation_time_id";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new ReservationTimeAvailability(
                        new ReservationTime(rs.getLong("id"), rs.getTime("start_at").toLocalTime()),
                        rs.getBoolean("already_booked")
                ), Date.valueOf(date), themeId);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
