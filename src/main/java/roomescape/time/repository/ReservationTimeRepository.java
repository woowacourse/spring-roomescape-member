package roomescape.time.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReservationTimeRepository {
    private static final RowMapper<ReservationTime> TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    resultSet.getString("start_at"));
    private static final RowMapper<ReservationUserTime> USER_TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationUserTime(
                    resultSet.getLong("id"),
                    resultSet.getString("start_at"),
                    resultSet.getBoolean("already_booked"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("id");
    }

    public long save(final ReservationTime reservationTime) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt().toString());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public ReservationTime findById(final long id) {
        final String sql = "select * from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, TIME_ROW_MAPPER, id);
    }

    public List<ReservationTime> findAll() {
        final String sql = "select * from reservation_time";
        return jdbcTemplate.query(sql, TIME_ROW_MAPPER);
    }

    public int deleteById(final long id) {
        final String sql = "delete from reservation_time where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        final String sql = "SELECT t.id, t.start_at, " +
                "EXISTS (SELECT 1 FROM reservation r WHERE r.time_id = t.id AND r.date = ? AND r.theme_id = ?) " +
                "AS already_booked " +
                "FROM reservation_time t";
        return jdbcTemplate.query(sql, USER_TIME_ROW_MAPPER, date, themeId);
    }
}
