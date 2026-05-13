package roomescape.domain.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.entity.ReservationTime;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {

    private static final String FIND_ALL_RESERVATION_TIMES_QUERY = """
            SELECT id, start_at
            FROM reservation_time
            """;

    private static final String FIND_RESERVATION_TIME_BY_ID_QUERY = """
            SELECT id, start_at
            FROM reservation_time
            WHERE id = :id
            """;

    private static final String DELETE_RESERVATION_TIME_BY_ID_QUERY = """
            DELETE FROM reservation_time
            WHERE id = :id
            """;

    private static final String UPDATE_RESERVATION_TIME_QUERY = """
            UPDATE reservation_time
            SET start_at = :start_at
            WHERE id = :id
            """;

    private static final String EXISTS_BY_START_AT_QUERY = """
            SELECT EXISTS (
                SELECT 1
                FROM reservation_time
                WHERE start_at = :start_at
            )
            """;

    private static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (resultSet, rowNumber) -> ReservationTime.of(
            resultSet.getLong("id"),
            resultSet.getObject("start_at", LocalTime.class)
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_RESERVATION_TIMES_QUERY,
                RESERVATION_TIME_ROW_MAPPER
        );
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("id", id);

            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    FIND_RESERVATION_TIME_BY_ID_QUERY,
                    parameters,
                    RESERVATION_TIME_ROW_MAPPER
            );

            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new IllegalArgumentException("reservationTime이 null 입니다.");
        }

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        Long generatedId = key.longValue();

        reservationTime.assignId(generatedId);

        return reservationTime;
    }

    @Override
    public int update(Long id, ReservationTime reservationTime) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("start_at", reservationTime.getStartAt());

        return jdbcTemplate.update(UPDATE_RESERVATION_TIME_QUERY, parameters);
    }

    @Override
    public int deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.update(
                DELETE_RESERVATION_TIME_BY_ID_QUERY,
                parameters
        );
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", startAt);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(EXISTS_BY_START_AT_QUERY, parameters, Boolean.class));
    }
}
