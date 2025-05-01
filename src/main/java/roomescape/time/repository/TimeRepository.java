package roomescape.time.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.Time;

@Repository
public class TimeRepository {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TimeRepository(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Time add(Time time) {
        Map<String, Object> parameters = Map.of("start_at", time.startAt());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Time(id, time.startAt());
    }

    public Optional<Time> findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = :id";

        Map<String, Object> parameter = Map.of("id", id);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createReservationTime(resultSet)));
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Time> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";

        return namedParameterJdbcTemplate.query(sql,
                (resultSet, rowNum) -> createReservationTime(resultSet));
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = :startAt";

        Map<String, Object> parameter = Map.of("startAt", startAt);

        return Boolean.TRUE.equals(
                namedParameterJdbcTemplate.queryForObject(sql, parameter, Boolean.class));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = :id";
        Map<String, Object> parameter = Map.of("id", id);

        namedParameterJdbcTemplate.update(sql, parameter);
    }

    private Time createReservationTime(ResultSet resultSet) throws SQLException {
        return new Time(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}
