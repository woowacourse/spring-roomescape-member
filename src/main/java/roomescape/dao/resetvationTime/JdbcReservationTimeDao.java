package roomescape.dao.resetvationTime;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> reservationTimeMapper = (resultSet, rowNum) -> new ReservationTime(
            resultSet.getLong("id"),
            resultSet.getObject("start_at", LocalTime.class)
    );

    public JdbcReservationTimeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeMapper);
    }

    @Override
    public ReservationTime create(final ReservationTime reservationTime) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>(Map.of(
                "start_at", reservationTime.getStartAt()));

        Number key = jdbcInsert.executeAndReturnKey(parameters);
        return new ReservationTime(key.longValue(), reservationTime.getStartAt());
    }

    @Override
    public boolean deleteIfNoReservation(long id) {
        String sql = """
                DELETE FROM reservation_time rt 
                WHERE rt.id = ? 
                AND NOT EXISTS (
                    SELECT 1 
                    FROM reservation r 
                    WHERE r.time_id = rt.id
                )
                """;
        return jdbcTemplate.update(sql, id) == 1;
    }

    @Override
    public Optional<ReservationTime> findById(final long id) {
        final String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationTimeMapper, id));
    }
}
