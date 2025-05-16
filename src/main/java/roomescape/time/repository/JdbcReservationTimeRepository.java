package roomescape.time.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.dto.ReservationTimeWithBookedDataResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final RowMapper<ReservationTime> ROW_MAPPER = (resultSet, rowNum) ->
        new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getObject("start_at", LocalTime.class)
        );
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public ReservationTime save(ReservationTime entity) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (:start_at)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", entity.getStartAt());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        final long id = keyHolder.getKey().longValue();
        return new ReservationTime(id, entity.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean deleteById(final Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        final int updated = jdbcTemplate.update(sql, params);
        return updated > 0;
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            ReservationTime timeEntity = jdbcTemplate.queryForObject(sql, params, ROW_MAPPER);
            return Optional.of(timeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTimeWithBookedDataResponse> findAllWithBooked(LocalDate date, final Long themeId) {
        String query = """
                SELECT
                    rt.id,
                    rt.start_at,
                    r.id IS NOT NULL as alreadyBooked
                FROM reservation_time as rt
                LEFT JOIN (
                    SELECT id, time_id
                    FROM reservation
                    WHERE date = :date AND theme_id = :themeId
                ) as r
                on r.time_id = rt.id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date.toString())
                .addValue("themeId", themeId);
        return jdbcTemplate.query(query, params, (resultSet, rowNum) -> {
            final long id = resultSet.getLong("id");
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);
            final boolean alreadyBooked = resultSet.getBoolean("alreadyBooked");
            return new ReservationTimeWithBookedDataResponse(id, startAt, alreadyBooked);
        });
    }

    
    @Override
    public Optional<ReservationTime> findByStartAt(LocalTime startAt) {
        String query = """
                SELECT
                    id,
                    start_at
                FROM reservation_time
                WHERE start_at = :startAt
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("startAt", startAt);
        try {
            ReservationTime timeEntity = jdbcTemplate.queryForObject(query, params, ROW_MAPPER);
            return Optional.of(timeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
