package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Component
public class ReservationDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        Reservation reservation = new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                time
        );
        return reservation;
    };

    public ReservationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation findById(Long id) {
        String sql = """
                select r.id, r.name, r.date, t.id as time_id, t.start_at from reservation r 
                join reservation_time t on r.time_id = t.id where r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Reservation> findAll() {
        String sql = """
                select r.id, r.name, r.date, t.id as time_id, t.start_at from reservation r 
                join reservation_time t on r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Long save(Reservation reservation) {
        String sql = "insert into reservation(name, date, time_id) values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
