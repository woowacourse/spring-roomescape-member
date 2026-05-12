package roomescape.repository;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.TimeSlot;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcTimeSlotRepository implements TimeSlotRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeSlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TimeSlot> findAll() {
        String sql = "SELECT id, start_at FROM time_slot";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public Optional<TimeSlot> findById(long timeId) {
        String sql = "SELECT id, start_at FROM time_slot where id = ?";
        List<TimeSlot> timeSlots = jdbcTemplate.query(sql, rowMapper(), timeId);
        return Optional.ofNullable(DataAccessUtils.singleResult(timeSlots));
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        SimpleJdbcInsert insert = createInsert();
        Map<String, Object> params = createParams(timeSlot);
        long reservationId = insert.executeAndReturnKey(params).longValue();
        return new TimeSlot(reservationId, timeSlot.startAt());
    }

    @Override
    public void deleteById(long timeId) {
        String sql = "DELETE FROM time_slot where id = ?";
        jdbcTemplate.update(sql, timeId);
    }

    @Override
    public int update(TimeSlot timeSlot) {
        String sql = "UPDATE time_slot SET start_at = ? WHERE id = ?";
        return jdbcTemplate.update(sql, timeSlot.startAt(), timeSlot.id());
    }
    
    private SimpleJdbcInsert createInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("time_slot")
                .usingGeneratedKeyColumns("id");
    }

    private Map<String, Object> createParams(TimeSlot timeSlot) {
        return Map.of("start_at", timeSlot.startAt());
    }

    private RowMapper<TimeSlot> rowMapper() {
        return (rs, rowNum) -> new TimeSlot(
                rs.getLong("id"),
                rs.getObject("start_at", LocalTime.class)
        );
    }
}
