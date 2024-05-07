package roomescape.time.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.time.response.AvailableTime;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, __) -> new ReservationTime(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    private final RowMapper<AvailableTime> availableTimeRowMapper = (resultSet, __) -> new AvailableTime(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime(),
            resultSet.getBoolean("is_available")
    );

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM RESERVATION_TIME", reservationTimeRowMapper);
    }

    public ReservationTime findById(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM RESERVATION_TIME WHERE ID = ?", reservationTimeRowMapper, id
        );
    }

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.startAt());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return findById(id);
    }

    public boolean exists(LocalTime startAt) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM RESERVATION_TIME WHERE START_AT = ?",
                Integer.class,
                startAt
        );
        return count != null && count > 0;
    }

    public int delete(long id) {
        return jdbcTemplate.update("DELETE FROM RESERVATION_TIME WHERE ID = ?", id);
    }

    public List<AvailableTime> available(LocalDate date, long themeId) {
        String query = "SELECT " +
                "rt.id, " +
                "rt.start_at, " +
                "CASE WHEN r.id IS NULL THEN FALSE ELSE TRUE END AS is_available " +
                "FROM " +
                "reservation_time rt " +
                "LEFT JOIN " +
                "reservation r ON rt.id = r.time_id AND r.date = ? AND r.theme_id = ? " +
                "ORDER BY " +
                "rt.start_at";
        return jdbcTemplate.query(query, availableTimeRowMapper, date, themeId);
    }
}
