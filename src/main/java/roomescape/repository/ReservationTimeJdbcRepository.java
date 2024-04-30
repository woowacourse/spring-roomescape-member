package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER = (selectedTime, rowNum) ->
            new ReservationTime(selectedTime.getLong("id"), selectedTime.getString("start_at"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationTimeInsert;

    public ReservationTimeJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_times")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        final String startAt = reservationTime.getFormattedTime();
        final SqlParameterSource timeParameters = new MapSqlParameterSource()
                .addValue("start_at", startAt);
        final Long savedTimeId = reservationTimeInsert.executeAndReturnKey(timeParameters).longValue();
        return new ReservationTime(savedTimeId, startAt);
    }

    public Optional<ReservationTime> findById(final Long id) {
        final String selectQuery = "SELECT id, start_at FROM reservation_times WHERE id = ?";
        try {
            final ReservationTime time = jdbcTemplate.queryForObject(selectQuery, ROW_MAPPER, id);
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        final String selectQuery = "SELECT id, start_at FROM reservation_times";
        return jdbcTemplate.query(selectQuery, ROW_MAPPER)
                .stream()
                .toList();
    }

    public boolean deleteById(final Long id) {
        final int affectedRowCount = jdbcTemplate.update("DELETE FROM reservation_times WHERE id = ?", id);
        return affectedRowCount == 1;
    }
}
