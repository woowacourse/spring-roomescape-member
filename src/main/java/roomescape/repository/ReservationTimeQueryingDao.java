package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeResponse;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeQueryingDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeQueryingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getObject("start_at", LocalTime.class)
        );
        return reservationTime;
    };

    public Optional<ReservationTime> findReservationTimeById(long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id);
            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAllReservationTime() {
        String sql = "select id, start_at from reservation_time";
        return  jdbcTemplate.query(sql, reservationTimeRowMapper);
    }
}
