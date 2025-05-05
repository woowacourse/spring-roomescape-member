package roomescape.reservationtime.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dto.response.AvailableTimeResponse;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    resultSet.getObject("start_at", LocalTime.class)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time ORDER BY start_at";
        return jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        Map<String, Object> parameter = Map.of("start_at", reservationTime.getStartAt());
        Long newId = simpleJdbcInsert.executeAndReturnKey(parameter).longValue();
        return reservationTime.withId(newId);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = ?";

        try {
            ReservationTime foundReservationTime = jdbcTemplate.queryForObject(sql, RESERVATION_TIME_ROW_MAPPER, id);
            return Optional.ofNullable(foundReservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AvailableTimeResponse> findByDateAndThemeIdWithBooked(LocalDate date, Long themeId) {
        String sql = """
                SELECT rt.id, rt.start_at, r.id is not null as already_booked
                FROM reservation_time rt
                LEFT JOIN reservation r
                  ON rt.id = r.time_id
                  AND r.date = ?
                  AND r.theme_id = ?
                """;
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new AvailableTimeResponse(
                        rs.getLong("id"),
                        rs.getTime("start_at").toLocalTime(),
                        rs.getBoolean("already_booked")
                ),
                date, themeId
        );
    }
}
