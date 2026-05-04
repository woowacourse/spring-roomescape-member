package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

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

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_url")
        );

        return new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("reservation_name"),
                resultSet.getObject("reservation_date", LocalDate.class),
                reservationTime,
                theme
        );
    };

    public Reservation findReservationById(long id) {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, t.start_at, th.id as theme_id, th.name as theme_name, th.description as theme_description, th.url as theme_url
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on th.id = r.theme_id
                where r.id = ?
                """;
        
        
        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    public List<Reservation> findAllReservations() {
        String sql = """
                select r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, r.time_id, t.start_at, th.id as theme_id, th.name as theme_name, th.description as theme_description, th.url as theme_url
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on th.id = r.theme_id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }
}
