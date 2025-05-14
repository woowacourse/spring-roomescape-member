package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (resultSet, rowNumber) ->
            new ReservationTime(resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime());

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("time_id");
    }

    @Override
    public long insert(final ReservationTime reservationTime) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        final Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existsByStartAt(final LocalTime startAt) {
        final String query = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, startAt));
    }

    @Override
    public List<ReservationTime> findAll() {
        final String query = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(query, RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public Optional<ReservationTime> findById(final long id) {
        final String query = "SELECT * FROM reservation_time WHERE time_id = ?";
        final List<ReservationTime> reservationTimes = jdbcTemplate.query(query, RESERVATION_TIME_ROW_MAPPER, id);
        return reservationTimes.stream()
                .findFirst();
    }

    @Override
    public List<ReservationTime> findAllBookedTime(final LocalDate date, final long themeId) {
        final String query = """
                SELECT rt.time_id,
                rt.start_at
                FROM reservation_time as rt
                JOIN reservation AS r
                ON r.time_id = rt.time_id
                WHERE r.date = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(query, RESERVATION_TIME_ROW_MAPPER, date, themeId);
    }

    @Override
    public boolean deleteById(final long id) {
        final String query = "DELETE FROM reservation_time where time_id = ?";
        final int deleted = jdbcTemplate.update(query, id);
        return deleted > 0;
    }
}
