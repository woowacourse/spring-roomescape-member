package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.Reservation;
import roomescape.business.ReservationTime;

@Repository
public class ReservationRepository implements GeneralRepository<Reservation> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String query = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value "
                + "FROM reservation as r "
                + "inner join reservation_time as t "
                + "on r.time_id = t.id ";
        return jdbcTemplate.query(query, (rs, rowNum) -> new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                new ReservationTime(rs.getLong("time_id"), rs.getObject("time_value", LocalTime.class))));
    }

    @Override
    public Reservation findById(Long id) {
        String query = "SELECT r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at as time_value "
                + "FROM reservation as r "
                + "inner join reservation_time as t "
                + "on r.time_id = t.id "
                + "where r.id = ?";
        return jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("name"),
                        rs.getObject("date", LocalDate.class),
                        new ReservationTime(
                                rs.getLong("time_id"),
                                rs.getObject("time_value", LocalTime.class)
                        )
                ), id);
    }

    @Override
    public Long add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        Long id = (Long) jdbcInsert.executeAndReturnKey(parameters);
        reservation.setId(id);
        return id;
    }

    @Override
    public void delete(Long id) {
        String query = "delete from RESERVATION where id=?";
        jdbcTemplate.update(query, id);
    }
}
