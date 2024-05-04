package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.Time;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.List;

@Repository
public class TimeRepository {

    private static final RowMapper<Time> ROW_MAPPER = (resultSet, rowNum) -> new Time(
            resultSet.getLong("id"),
            resultSet.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public TimeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public Time findById(Long timeId) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, timeId);
    }

    public List<Time> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public List<Time> findByStartAt(LocalTime startAt) {
        String sql = "SELECT * FROM reservation_time WHERE start_at = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, startAt);
    }

    public Time insert(Time requestTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", requestTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Time(id, requestTime.getStartAt());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
