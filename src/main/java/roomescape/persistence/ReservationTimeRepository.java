package roomescape.persistence;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.ReservationTime;

@Repository
public class ReservationTimeRepository implements GeneralRepository<ReservationTime> {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = "select id, start_at from reservation_time";
        return jdbcTemplate.query(query, (rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        ));
    }

    @Override
    public ReservationTime findById(Long id) {
        String query = "select id, start_at from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)), id);
    }

    @Override
    public Long add(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start_at", reservationTime.getStartAt());
        Long id = (Long) jdbcInsert.executeAndReturnKey(parameters);
        reservationTime.setId(id);
        return id;
    }

    @Override
    public void delete(Long id) {
        String query = "delete from reservation_time where id=?";
        jdbcTemplate.update(query, id);
    }
}
