package roomescape.reservationtime.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> reservationTimeMapper = (resultSet, rowNum) -> ReservationTime.load(
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
        return ReservationTime.load(key.longValue(), reservationTime.getStartAt());
    }

    @Override
    public void delete(ReservationTime reservationTime) {
        final String sql = "DELETE FROM reservation_time rt WHERE rt.id = ?";
        jdbcTemplate.update(sql, reservationTime.getId());
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        final String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.query(sql, reservationTimeMapper, id).stream()
                .findFirst();
    }

    @Override
    public Optional<ReservationTime> findByIdAndDateAndTheme(final Long id, final Long themeId, final LocalDate date) {
        final String sql = """
                SELECT id, start_at
                FROM reservation_time rt
                WHERE EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.time_id = ? AND r.theme_id = ? AND r.date = ?
                )
                """;
        return jdbcTemplate.query(sql, reservationTimeMapper, id, themeId, date).stream()
                .findFirst();
    }
}
