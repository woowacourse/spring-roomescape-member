package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.Time;

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

    public Time findById(Long id) {
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
    }

    public List<Time> findByStartAt(LocalTime startAt) {
        String sql = "SELECT * FROM reservation_time WHERE start_at = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, startAt);
    }

    public List<Time> findAll() {
        String sql = "SELECT * FROM reservation_time";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Time save(Time requestTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", requestTime.getStartAt());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Time(id, requestTime.getStartAt());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
