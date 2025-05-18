package roomescape.infrastructure.jdbctemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.ReservationTime;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.response.AvailableTimesResponseDto;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public H2ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (rs, rowNum) -> (
            new ReservationTime(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            )
    );

    @Override
    public List<ReservationTime> findAll() {
        String query = """
                SELECT id, start_at 
                FROM reservation_time
                """;
        return jdbcTemplate.query(query, reservationTimeRowMapper);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String query = """
                SELECT id, start_at 
                FROM reservation_time 
                WHERE id = ?
                """;
        return jdbcTemplate.query(query, reservationTimeRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Long add(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start_at", reservationTime.getStartAt());
        return (Long) jdbcInsert.executeAndReturnKey(parameters);
    }

    @Override
    public void deleteById(Long id) {
        String query = """
                DELETE FROM reservation_time
                WHERE id = ?
                """;
        jdbcTemplate.update(query, id);
    }

    @Override
    public List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId) {
        String query = """
                SELECT
                    t.id,
                    t.start_at,
                    CASE
                        WHEN r.id IS NOT NULL THEN TRUE
                        ELSE FALSE
                    END AS is_booked
                FROM reservation_time t
                LEFT JOIN reservation r
                    ON t.id = r.time_id
                    AND r.date = ?
                    AND r.theme_id = ?
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new AvailableTimesResponseDto(
                        rs.getLong("id"),
                        rs.getObject("start_at", LocalTime.class),
                        rs.getBoolean("is_booked")
                ),
                date,
                themeId
        );
    }
}
