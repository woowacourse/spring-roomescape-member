package roomescape.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.row.TimeRow;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeJdbcDao implements TimeDao {
    private static final RowMapper<TimeRow> ROW_MAPPER = (resultSet, rowNum) -> new TimeRow(
            resultSet.getLong("time_id"),
            LocalTime.parse(resultSet.getString("start_at"))
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert timeInsert;


    public TimeJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.timeInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("times")
                .usingGeneratedKeyColumns("id")
                .usingColumns("start_at");
    }

    @Override
    public List<TimeRow> findAll() {
        String sql = """
                SELECT
                    id AS time_id,
                    start_at AS time_start_at
                FROM times
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<TimeRow> findById(Long id) {
        String sql = """
                SELECT
                    id AS time_id,
                    start_at AS time_start_at
                FROM times
                WHERE id = :id
                """;

        SqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(sql, params, ROW_MAPPER).stream()
                .findFirst();
    }

    @Override
    public TimeRow create(TimeRow time) {
        SqlParameterSource params = new MapSqlParameterSource("start_at", time.startAt());

        Long id = timeInsert.executeAndReturnKey(params).longValue();
        return new TimeRow(id, time.startAt());
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

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = """
                SELECT EXISTS(
                    SELECT 1 FROM times
                    WHERE start_at = :start_at
                )
                """;
        SqlParameterSource params = new MapSqlParameterSource("start_at", startAt);

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, params, Boolean.class));
    }
}
