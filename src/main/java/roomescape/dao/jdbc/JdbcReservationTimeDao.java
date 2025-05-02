package roomescape.dao.jdbc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeWithIsBookedGetResponse;
import roomescape.exception.TimeDoesNotExistException;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("reservation_time")
            .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, mapResultsToReservationTime());
    }

    public List<ReservationTimeWithIsBookedGetResponse> findByDateAndThemeIdWithIsBookedOrderByStartAt(LocalDate date, Long themeId) {
        String sql = """
            SELECT
              rt.id,
              rt.start_at,
              EXISTS(
                SELECT r.id
                FROM reservation as r
                WHERE r.time_id = rt.id
                    AND r.theme_id = ?
                    AND r.date = ?
              ) as isBooked
            FROM reservation_time as rt
            ORDER BY rt.start_at;
            """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new ReservationTimeWithIsBookedGetResponse(
                resultSet.getLong("id"),
                resultSet.getObject("start_at", LocalTime.class),
                resultSet.getBoolean("isBooked")
        ), themeId, date);
    }

    public ReservationTime findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mapResultsToReservationTime(), id);
        } catch (DataAccessException e) {
            throw new TimeDoesNotExistException();
        }
    }

    public ReservationTime add(ReservationTime reservationTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("start_at", reservationTime.getStartAt());

        Number key = jdbcInsert.executeAndReturnKey(param);
        return new ReservationTime(key.longValue(), reservationTime.getStartAt());
    }

    public boolean existByStartAt(LocalTime startAt) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation_time WHERE start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTime> mapResultsToReservationTime() {
        return (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime());
    }
}
