package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTime;

@Repository
@RequiredArgsConstructor
public class ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    );

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time", timeRowMapper);
    }

    public Optional<ReservationTime> findById(long id) {
        final String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, timeRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByStartAt(LocalTime startAt) {
        final String sql = """
        SELECT EXISTS (
            SELECT 1
            FROM reservation_time
            WHERE start_at = ?
        )
        """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.getId() == null) {
            return insert(reservationTime);
        }

        return merge(reservationTime);
    }

    private ReservationTime merge(ReservationTime reservationTime) {
        final String sql = "UPDATE reservation_time SET start_at = ? WHERE id = ?";
        final int affectedRows = jdbcTemplate.update(sql, reservationTime.getStartAt(), reservationTime.getId());

        if (affectedRows == 0) {
            return insert(reservationTime);
        }

        return reservationTime;
    }

    private ReservationTime insert(ReservationTime reservationTime) {
        if (reservationTime.getId() != null) {
            final String sql = "INSERT INTO reservation_time (id, start_at) VALUES (?, ?)";
            jdbcTemplate.update(sql, reservationTime.getId(), reservationTime.getStartAt());
            return reservationTime;
        }

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation_time (start_at) VALUES (?)",
                    new String[]{"id"}
            );

            ps.setObject(1, reservationTime.getStartAt());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return new ReservationTime(keyHolder.getKey().longValue(), reservationTime.getStartAt());
        }

        return reservationTime;
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }
}
