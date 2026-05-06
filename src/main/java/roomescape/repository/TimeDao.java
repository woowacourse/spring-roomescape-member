package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Time;

@Repository
public class TimeDao implements TimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Time> findAll() {
        String sql = "SELECT id, start_at FROM time";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public Time findById(long timeId) {
        String sql = "SELECT id, start_at FROM time where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), timeId);
    }

    @Override
    public Time save(Time time) {
        SimpleJdbcInsert insert = createInsert();
        Map<String, Object> params = createParams(time);
        long reservationId = insert.executeAndReturnKey(params).longValue();
        return new Time(reservationId, time.startAt());
    }

    @Override
    public void deleteById(long timeId) {
        String sql = "DELETE FROM time where id = ?";
        jdbcTemplate.update(sql, timeId);
    }

    private RowMapper<Time> rowMapper() {
        return (rs, rowNum) -> new Time(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    }

    private SimpleJdbcInsert createInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("time")
                .usingGeneratedKeyColumns("id");
    }

    private Map<String, Object> createParams(Time time) {
        return Map.of("start_at", time.startAt());
    }
}
