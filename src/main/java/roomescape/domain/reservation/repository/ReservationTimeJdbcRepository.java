package roomescape.domain.reservation.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.ReservationTime;

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

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeJdbcRepository(
            NamedParameterJdbcTemplate jdbcTemplate,
            DataSource dataSource
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                FIND_ALL_RESERVATION_TIMES_QUERY,
                reservationTimeRowMapper()
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
                    reservationTimeRowMapper()
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

        reservationTime.setId(generatedId);

        return reservationTime;
    }

    @Override
    public void deleteById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        jdbcTemplate.update(
                DELETE_RESERVATION_TIME_BY_ID_QUERY,
                parameters
        );
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNumber) -> new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
    }
}
