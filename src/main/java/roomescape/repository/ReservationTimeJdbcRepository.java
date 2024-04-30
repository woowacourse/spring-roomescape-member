package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER = (selectedTime, rowNum) ->
            new ReservationTime(selectedTime.getLong("id"), LocalTime.parse(selectedTime.getString("start_at")));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationTimeInsert;

    public ReservationTimeJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_times")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(reservationTime);
        final Long savedTimeId = reservationTimeInsert.executeAndReturnKey(parameterSource).longValue();
        return new ReservationTime(savedTimeId, reservationTime.getStartAt());
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        final String selectQuery = "SELECT id, start_at FROM reservation_times WHERE id = ?";
        try {
            final ReservationTime time = jdbcTemplate.queryForObject(selectQuery, ROW_MAPPER, id);
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        final String selectQuery = "SELECT id, start_at FROM reservation_times";
        return jdbcTemplate.query(selectQuery, ROW_MAPPER)
                .stream()
                .toList();
    }

    @Override
    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM reservation_times WHERE id = ?", id);
    }

    @Override
    public boolean existByStartAt(final LocalTime startAt) {
        String sql = """
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
}
