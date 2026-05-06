package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Time;

@Repository
public class TimeJdbcDao implements TimeDao {
    public static final RowMapper<Time> ROW_MAPPER = (resultSet, rowNum) -> new Time(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TimeJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Time> findAll() {
        String sql = """
                SELECT * FROM times
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<Time> findById(Long id) {
        String sql = """
                SELECT * FROM times
                WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(sql, params, ROW_MAPPER).stream()
                .findFirst();
    }

    @Override
    public Time insert(Time time) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("times")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new MapSqlParameterSource("start_at", time.getStartAt());

        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Time(id, time.getStartAt());
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM times
                WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.update(sql, params);
    }
}
