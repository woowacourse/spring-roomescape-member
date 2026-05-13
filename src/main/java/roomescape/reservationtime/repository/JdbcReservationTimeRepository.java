package roomescape.reservationtime.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.exception.ReservationTimeResourceInUseException;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    public static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (rs, rowNum) ->
            ReservationTime.of(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate,
                                         NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                         DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationTime.toEntity(reservationTime, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            ReservationTime reservationTime = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    params,
                    RESERVATION_TIME_ROW_MAPPER
            );
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time ORDER BY id";
        return jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public List<ReservationTime> findAvailableReservationTimes(LocalDate date, Long themeId) {
        String sql = """
                SELECT
                    rt.id,
                    rt.start_at
                FROM reservation_time rt
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.time_id = rt.id
                        AND r.date = :date
                        AND r.theme_id = :themeId
                )
                ORDER BY rt.id
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);
        return namedParameterJdbcTemplate.query(sql, params, RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public void deleteById(Long id) {
        int affectedRows = executeDelete(id);
        if (affectedRows == 0) {
            throw new ReservationTimeNotFoundException(id);
        }
    }

    private int executeDelete(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            return namedParameterJdbcTemplate.update(sql, params);
        } catch (DataIntegrityViolationException e) {
            throw new ReservationTimeResourceInUseException(id);
        }
    }
}
