package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.ReservationTimeInfoResponse;
import roomescape.dto.reservation.ReservationTimeInfosResponse;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class TimeDao {

    private static final RowMapper<Time> ROW_MAPPER = (resultSet, rowNum) -> new Time(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TimeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public Time findById(final Long timeId) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, timeId);
    }

    public List<Time> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public List<Time> findByStartAt(final LocalTime startAt) {
        String sql = "SELECT * FROM reservation_time WHERE start_at = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, startAt);
    }

    public ReservationTimeInfosResponse findByDateAndThemeId(final LocalDate date, final Long themeId) {
        String sql = """
                SELECT
                    rt.id, start_at, IFNULL(is_reserved, 'False') AS is_reserved
                FROM reservation_time rt
                LEFT JOIN (
                    SELECT rt.id, 'True' AS is_reserved
                    FROM reservation_time rt
                    JOIN reservation r ON r.time_id = rt.id
                    WHERE r.date = ? AND r.theme_id = ?
                ) rvt ON rt.id = rvt.id
                """;

        List<ReservationTimeInfoResponse> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            long timeId = rs.getLong("id");
            LocalTime startAt = rs.getTime("start_at").toLocalTime();
            boolean isReserved = rs.getBoolean("is_reserved");

            return new ReservationTimeInfoResponse(timeId, startAt, isReserved);
        }, date, themeId);

        return new ReservationTimeInfosResponse(results);
    }

    public Time insert(final Time requestTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", requestTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Time(id, requestTime.getStartAt());
    }


    public int deleteById(final Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
