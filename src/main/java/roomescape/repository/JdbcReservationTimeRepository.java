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
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER = ((rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    ));

    private static final RowMapper<ReservationTimeWithBookStatusResponse> WITH_BOOK_STATUS_ROW_MAPPER = ((rs, rowNum) -> new ReservationTimeWithBookStatusResponse(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime(),
            rs.getBoolean("already_booked")
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
        return jdbcTemplate.query("SELECT id, start_at FROM reservation_time", ROW_MAPPER);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTimeWithBookStatusResponse> findByDateAndThemeIdWithBookStatus(LocalDate date,
                                                                                          Long themeId) {
        String sql = """
                SELECT 
                    t.id,
                    t.start_at,
                    CASE
                        WHEN r.id IS NULL THEN FALSE
                        ELSE TRUE
                    END AS already_booked
                FROM reservation_time AS t
                LEFT JOIN reservation AS r ON t.id = r.time_id AND r.date = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(sql, WITH_BOOK_STATUS_ROW_MAPPER, date, themeId);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> reservationTimeRow = new HashMap<>();
        reservationTimeRow.put("start_at", reservationTime.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(reservationTimeRow).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
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
}
