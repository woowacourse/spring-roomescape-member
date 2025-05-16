package roomescape.dao;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<ReservationTime> rowMapper = (resultSet, rowNum) ->
        new ReservationTime(
            resultSet.getLong("id"),
            LocalTime.parse(
                resultSet.getString("start_at"),
                DateTimeFormatter.ofPattern("HH:mm")
            )
        );

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("reservation_time")
            .usingGeneratedKeyColumns("id");
    }

    public Optional<ReservationTime> findById(Long id) {
        try {
            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                "select * from reservation_time where id = ?",
                rowMapper,
                id
            );
            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start_at",
            reservationTime.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")).toString());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("select * from reservation_time", rowMapper);
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }
}
