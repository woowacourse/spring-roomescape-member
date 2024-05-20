package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.AvailableReservationTimeRowMapper;
import roomescape.dao.mapper.ReservationTimeRowMapper;
import roomescape.domain.AvailableReservationTime;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ReservationTimeRowMapper reservationTimeRowMapper;
    private final AvailableReservationTimeRowMapper availableReservationTimeRowMapper;

    public ReservationTimeDao(final JdbcTemplate jdbcTemplate,
                              final DataSource dataSource,
                              final ReservationTimeRowMapper reservationTimeRowMapper,
                              final AvailableReservationTimeRowMapper availableReservationTimeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.reservationTimeRowMapper = reservationTimeRowMapper;
        this.availableReservationTimeRowMapper = availableReservationTimeRowMapper;
    }

    public ReservationTime create(final ReservationTime reservationTime) {
        final var params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAtAsString());
        final var id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationTime.of(id, reservationTime.getStartAtAsString());
    }

    public boolean existsByStartAt(final String startAt) {
        final var sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, startAt));
    }

    public Optional<ReservationTime> findById(final Long id) {
        final var sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> getAll() {
        final var sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public List<AvailableReservationTime> findAvailable(final ReservationDate date, final long themeId) {
        final var sql = """
                SELECT
                t.start_at AS start_at,
                t.id AS time_id,
                r.id IS NOT NULL AS already_booked
                FROM reservation_time AS t
                LEFT OUTER JOIN reservation AS r
                ON t.id = r.time_id AND r.date = ? AND r.theme_id = ?;
                """;
        return jdbcTemplate.query(sql, availableReservationTimeRowMapper, date.asString(), themeId);
    }

    public void delete(final long id) {
        final var sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
