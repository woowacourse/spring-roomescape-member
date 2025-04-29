package roomescape.repository;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.service.reservation.ReservationTime;

@Repository
public class ReservationTimeDao {

    public static final String RESERVATION_TIME_TABLE = "reservation_time";
    public static final String RESERVATION_TIME_PK = "id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationTimeInserter;
    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    LocalTime.parse(resultSet.getString("start_at"))
            );

    public ReservationTimeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeInserter = new SimpleJdbcInsert(dataSource)
                .withTableName(RESERVATION_TIME_TABLE)
                .usingGeneratedKeyColumns(RESERVATION_TIME_PK);
    }

    public ReservationTime createReservationTime(final ReservationTime reservationTime) {
        final long id = insertReservationTimeAndRetrieveKey(reservationTime);
        return getReservationTimeById(id);
    }

    public List<ReservationTime> getReservationTimes() {
        final String sql = "SELECT id, start_at from reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public ReservationTime getReservationTimeById(final long id) {
        final String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id);
    }

    public boolean isExists(final LocalTime reservationTime) {
        final String sql = "SELECT COUNT(*) FROM reservation_time WHERE start_at = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, reservationTime);
        return count > 0;
    }

    public void deleteReservationTimeById(final long id) {
        final String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private long insertReservationTimeAndRetrieveKey(final ReservationTime reservationTime) {
        final Map<String, Object> parameters = new HashMap<>(Map.of("start_at", reservationTime.getStartAt()));
        return (long) reservationTimeInserter.executeAndReturnKey(parameters);
    }
}
