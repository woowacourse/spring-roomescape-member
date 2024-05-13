package roomescape.time.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeStatus;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeRepository {
    private static final RowMapper<ReservationTime> TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime());
    private static final RowMapper<ReservationTimeStatus> USER_TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTimeStatus(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime(),
                    resultSet.getBoolean("already_booked"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("RESERVATION_TIME")
                .usingGeneratedKeyColumns("id");
    }

    public long save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt().toString());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, TIME_ROW_MAPPER);
    }

    public Optional<ReservationTime> findById(long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";

        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, TIME_ROW_MAPPER, id);
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int delete(long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    public List<ReservationTimeStatus> findByDateAndThemeId(String date, long themeId) {
        String sql = """
                SELECT t.id, t.start_at,
                EXISTS (SELECT 1 FROM reservation r WHERE r.time_id = t.id AND r.date = ? AND r.theme_id = ?)
                AS already_booked
                FROM reservation_time t""";

        return jdbcTemplate.query(sql, USER_TIME_ROW_MAPPER, date, themeId);
    }
}
