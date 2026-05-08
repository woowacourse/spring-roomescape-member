package roomescape.domain.time.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
            .withTableName("reservation_time")
            .usingColumns("start_at")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Time save(Time time) {
        Map<String, Object> args = Map.of("start_at", time.getStartAt());
        Long generatedKey = simpleJdbcInsert.executeAndReturnKey(args)
            .longValue();

        return Time.reconstruct(generatedKey, time.getStartAt());
    }

    @Override
    public List<Time> findAllTimes() {
        return jdbcTemplate.query(
            "SELECT id, start_at FROM reservation_time",
            this::mapTime
        );
    }

    @Override
    public Optional<Time> findTimeById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, parameters, this::mapTime));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteTimeById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    private Time mapTime(ResultSet resultSet, int rowNum) throws SQLException {
        return Time.reconstruct(
            resultSet.getLong("id"),
            LocalTime.parse(resultSet.getString("start_at"))
        );
    }
}
