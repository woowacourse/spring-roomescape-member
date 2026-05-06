package roomescape.domain.time.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.entity.Time;

@Repository
public class JdbcTimeRepository implements TimeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcTimeRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("time")
            .usingColumns("start_at")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Time save(Time time) {
        Map<String, Object> args = Map.of("start_at", time.getStartAt());

        long generatedKey = simpleJdbcInsert.executeAndReturnKey(args).longValue();

        return Time.reconstruct(generatedKey, time.getStartAt());
    }

    @Override
    public List<Time> findAllTimes() {
        String sql = "SELECT id, start_at FROM time";
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> Time.reconstruct(
                rs.getLong("id"),
                LocalTime.parse(rs.getString("start_at"))
            )
        );
    }

    @Override
    public Optional<Time> findTimeById(Long id) {
        String sql = "SELECT id, start_at FROM time WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        try {
            Time time = jdbcTemplate.queryForObject(
                sql,
                parameters,
                (resultSet, rowNum) -> Time.reconstruct(
                    resultSet.getLong("id"),
                    LocalTime.parse(resultSet.getString("start_at"))
                )
            );
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteTimeById(Long id) {
        final String sql = "DELETE FROM time WHERE id = :id";
        final SqlParameterSource parameters = new MapSqlParameterSource("id", id);

        jdbcTemplate.update(sql, parameters);
    }
}
