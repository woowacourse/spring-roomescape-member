package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationTimeDao implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public ReservationTime findById(long timeId) {
        String sql = "SELECT id, start_at FROM reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), timeId);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SimpleJdbcInsert insert = createInsert();
        Map<String, Object> params = createParams(reservationTime);
        long reservationId = insert.executeAndReturnKey(params).longValue();
        return new ReservationTime(reservationId, reservationTime.startAt());
    }

    @Override
    public void deleteById(long timeId) {
        String sql = "DELETE FROM reservation_time where id = ?";
        jdbcTemplate.update(sql, timeId);
    }

    private RowMapper<ReservationTime> rowMapper() {
        return (rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    }

    private SimpleJdbcInsert createInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    private Map<String, Object> createParams(ReservationTime reservationTime) {
        return Map.of("start_at", reservationTime.startAt());
    }
}
