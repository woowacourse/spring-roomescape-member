package roomescape.infrastructure;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> ROW_MAPPER = (resultSet, rowNum) -> ReservationTime.afterSave(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        final Number id = insert.executeAndReturnKey(Map.of(
                "start_at", reservationTime.getStartAt()
        ));

        return ReservationTime.afterSave(
                id.longValue(),
                reservationTime.getStartAt()
        );
    }

    @Override
    public Optional<ReservationTime> findById(long timeId) {
        try {
            final String sql = "SELECT * FROM reservation_time WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, timeId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> ReservationTime.afterSave(
                        resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime()
                ));
    }

    @Override
    public boolean existById(final long timeId) {
        final String sql = "SELECT COUNT(*) FROM reservation_time WHERE id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existByTime(LocalTime createTime) {
        final String sql = "SELECT COUNT(*) FROM reservation_time WHERE start_at = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, createTime);
        return count != null && count > 0;
    }

    @Override
    public boolean existBetween(final LocalTime startInclusive, final LocalTime endExclusive) {
        final String sql = """
                SELECT COUNT(*) FROM reservation_time
                WHERE start_at >= ? AND start_at < ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, startInclusive, endExclusive);
        return count != null && count > 0;
    }

    @Override
    public List<ReservationTime> findAvailableReservationTimesByDateAndThemeId(LocalDate date, long themeId) {
        final String sql = """
                SELECT *
                FROM reservation_time
                WHERE id NOT IN (
                    SELECT rt.id
                    FROM reservation_time AS rt
                    INNER JOIN reservation AS r
                    ON r.time_id = rt.id
                    WHERE r.date = ?
                    AND r.theme_id = ?
                )
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER, date, themeId);
    }

    @Override
    public void deleteById(long timeId) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, timeId);
    }

}
