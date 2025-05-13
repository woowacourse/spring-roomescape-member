package roomescape.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.model.ReservationTime;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private static final RowMapper<ReservationTime> ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime()
            );

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM reservation_time WHERE id = ?",
                ROW_MAPPER,
                id
        );
    }

    @Override
    public boolean existByTimeValue(LocalTime startAt) {
        String sql = "SELECT EXISTS (SELECT 1 from reservation_time where start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt.toString());
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start_at", reservationTime.getStartAt().toString());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation_time", ROW_MAPPER);
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }
}
