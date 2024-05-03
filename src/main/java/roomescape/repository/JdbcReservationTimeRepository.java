package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER = ((rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    ));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation_time", ROW_MAPPER);
    }

    @Override
    public ReservationTime insert(ReservationTime reservationTime) {
        Map<String, Object> reservationTimeRow = new HashMap<>();
        reservationTimeRow.put("start_at", reservationTime.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(reservationTimeRow).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existByStartAt(LocalTime startAt) {
        String sql = """
                SELECT EXISTS ( 
                    SELECT 1 
                    FROM reservation_time 
                    WHERE start_at = ?
                );
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    @Override
    public List<ReservationTime> findReservedTimes(LocalDate date, Long themeId) {
        String sql = """
                SELECT t.id, t.start_at
                FROM reservation_time AS t
                INNER JOIN reservation AS r ON t.id = r.time_id
                WHERE r.date = ? AND r.theme_id = ?;  
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, date, themeId);
    }
}
