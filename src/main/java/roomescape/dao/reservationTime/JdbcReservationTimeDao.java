package roomescape.dao.reservationTime;

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
import roomescape.domain.ReservationTime;
import roomescape.support.page.PageRequest;

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
        final String sql = """
                SELECT id, start_at
                FROM reservation_time
                """;
        return jdbcTemplate.query(sql, reservationTimeMapper);
    }

    @Override
    public List<ReservationTime> findAllWithPaging(final PageRequest pageRequest) {
        final int offset = pageRequest.getPageNo() * pageRequest.getPageSize();
        final String sortDirection = pageRequest.getSortDir().equalsIgnoreCase("asc") ? "ASC" : "DESC";

        final String sql = String.format(
                "SELECT id, start_at FROM reservation_time ORDER BY %s %s LIMIT ? OFFSET ?",
                pageRequest.getSortBy(), sortDirection);

        return jdbcTemplate.query(sql, reservationTimeMapper, pageRequest.getPageSize(), offset);
    }

    @Override
    public long countAll() {
        final String sql = """
                SELECT COUNT(*)
                FROM reservation_time
                """;
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public ReservationTime create(final ReservationTime reservationTime) {
        final SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>(Map.of(
                "start_at", reservationTime.getStartAt()));

        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return new ReservationTime(key.longValue(), reservationTime.getStartAt());
    }

    @Override
    public boolean deleteIfNoReservation(final long id) {
        final String sql = """
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
        final String sql = """
                SELECT *
                FROM reservation_time 
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, reservationTimeMapper, id).stream()
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAllReservedByThemeAndDate(final long themeId, final LocalDate date) {
        final String sql = """
                SELECT id, start_at
                FROM reservation_time rt
                WHERE EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.theme_id = ? AND r.date = ? AND r.time_id = rt.id
                )
                """;
        return jdbcTemplate.query(sql, reservationTimeMapper, themeId, date);
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = """
                SELECT COUNT(*)
                FROM reservation_time
                WHERE id = ?
                """;
        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
