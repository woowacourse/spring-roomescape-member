package roomescape.time.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.Time;
import roomescape.time.dto.AvailableTimeResponse;

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
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = :id";

        Map<String, Object> parameter = Map.of("id", id);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, parameter,
                    (resultSet, rowNum) -> createReservationTime(resultSet)));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<AvailableTimeResponse> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = "SELECT t.id, t.start_at, (r.id IS NOT NULL) AS already_booked "
                + "FROM reservation_time AS t "
                + "LEFT JOIN reservation AS r "
                + "ON t.id = r.time_id "
                + "AND r.date = :date "
                + "AND r.theme_id = :themeId";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", date);
        parameters.put("themeId", themeId);

        return namedParameterJdbcTemplate.query(sql, parameters,
                (resultSet, rowNum) -> new AvailableTimeResponse(
                        resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime(),
                        resultSet.getBoolean("already_booked")
                ));
    }

    public List<Time> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";

        return namedParameterJdbcTemplate.query(sql,
                (resultSet, rowNum) -> createReservationTime(resultSet));
    }

    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = :startAt)";

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
