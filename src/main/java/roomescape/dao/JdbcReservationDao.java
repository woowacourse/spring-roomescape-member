package roomescape.dao;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.Reservation;
import roomescape.mapper.ReservationMapper;

@Component
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = """
        select r.id as id, r.name, r.date, rt.id as time_id, rt.start_at
        from reservation as r 
        inner join reservation_time as rt 
        on r.time_id = rt.id
        """;
        List<Reservation> reservations = jdbcTemplate.query(
                sql,
                new ReservationMapper()
        );
        return reservations;
    }

    public long create(Reservation newReservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (name, date, time_id) values (?, ?, ?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            sql,
                            new String[]{"id"}
                    );
                    ps.setString(1, newReservation.getName());
                    ps.setObject(2, newReservation.getDate());
                    ps.setLong(3, newReservation.getTime().getId());
                    return ps;
                },
                keyHolder
        );
        return keyHolder.getKey().longValue();
    }

    public void deleteById(Id id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(
                sql,
                id.value()
        );
    }
}
