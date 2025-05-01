package roomescape.time.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.entity.ReservationTimeEntity;
import roomescape.time.repository.dto.AvailableTimeResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTimeEntity save(ReservationTimeEntity entity) {
        String sql = "INSERT INTO reservation_time (start_at) VALUES (:start_at)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", entity.getFormattedTime());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        final long id = keyHolder.getKey().longValue();
        return new ReservationTimeEntity(id, entity.getStartAt());
    }

    @Override
    public List<ReservationTimeEntity> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            LocalTime time = resultSet.getObject("start_at", LocalTime.class);
            return new ReservationTimeEntity(id, time);
        });
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
    public Optional<ReservationTimeEntity> findById(final Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            ReservationTimeEntity timeEntity = jdbcTemplate.queryForObject(sql, params, (resultSet, rowNum) -> {
                LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);
                return new ReservationTimeEntity(id, startAt);
            });
            return Optional.of(timeEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, final Long themeId) {
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
            return new AvailableTimeResponse(id, startAt, alreadyBooked);
        });
    }
}
