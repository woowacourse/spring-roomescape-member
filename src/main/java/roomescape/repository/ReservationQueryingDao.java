package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationQueryingDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationQueryingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("start_at", LocalTime.class)
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getObject("reservation_date", LocalDate.class),
                reservationTime
        );
    };

    public Reservation findReservationById(long id) {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, t.start_at
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                where r.id = ?
                """;
        
        
        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    public List<Reservation> findAllReservations() {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, t.start_at
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }
}
