package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNumber) -> {
        long reservationId = resultSet.getLong("reservation_id");
        String name = resultSet.getString("name");
        LocalDate date = resultSet.getDate("date").toLocalDate();
        long timeId = resultSet.getLong("time_id");
        LocalTime timeValue = resultSet.getTime("time_value").toLocalTime();

        ReservationTime reservationTime = new ReservationTime(timeId, timeValue);

        return new Reservation(reservationId, name, date, reservationTime);
    };

    @Override
    public long add(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int update = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into reservation (name,date,time_id) values(?,?,?)",
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getReservationTime().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id as reservation_id,
                        r.name,
                        r.date,
                        t.id as time_id,
                        t.start_at as time_value
                    FROM reservation as r
                    inner join reservation_time as t
                    on r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation where id=?";
        jdbcTemplate.update(sql, id);
    }
}
