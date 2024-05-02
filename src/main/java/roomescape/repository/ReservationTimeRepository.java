package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.model.ReservationTime;

@Repository
public class ReservationTimeRepository {

    private static final RowMapper<ReservationTime> TIME_ROW_MAPPER = (selectedTime, rowNum) ->
            new ReservationTime(selectedTime.getLong("id"), LocalTime.parse(selectedTime.getString("start_at")));
    private static final RowMapper<ReservationTimeBookedResponse> TIME_WITH_BOOKED_ROW_MAPPER = (selectedTime, rowNum) ->
            new ReservationTimeBookedResponse(selectedTime.getLong("id"), LocalTime.parse(selectedTime.getString("start_at")), selectedTime.getBoolean("already_booked"));


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationTimeInsert;

    public ReservationTimeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_times")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservationTime);
        final Long savedTimeId = reservationTimeInsert.executeAndReturnKey(parameterSource).longValue();
        return new ReservationTime(savedTimeId, reservationTime.getStartAt());
    }

    public Optional<ReservationTime> findById(final Long id) {
        final String selectQuery = "SELECT id, start_at FROM reservation_times WHERE id = ?";
        try {
            final ReservationTime time = jdbcTemplate.queryForObject(selectQuery, TIME_ROW_MAPPER, id);
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        final String selectQuery = "SELECT id, start_at FROM reservation_times";
        return jdbcTemplate.query(selectQuery, TIME_ROW_MAPPER);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM reservation_times WHERE id = ?", id);
    }

    public boolean existByStartAt(final LocalTime startAt) {
        final String sql = """
                SELECT
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservation_times
                        WHERE start_at = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, startAt));
    }

    public List<ReservationTimeBookedResponse> findTimesWithBooked(final LocalDate date, final Long themeId) {
        final String sql = """
                SELECT 
                    rt.start_at, 
                    rt.id, 
                    r.id is not null AS already_booked        
                FROM reservation_times AS rt
                LEFT JOIN
                    (SELECT id, time_id
                    FROM reservations
                    WHERE date = ? AND theme_id = ?) AS r
                    ON rt.id = r.time_id
                ORDER BY start_at
                """;

        return jdbcTemplate.query(sql, TIME_WITH_BOOKED_ROW_MAPPER, date, themeId);
    }
}
