package roomescape.dao.jdbc;

import java.sql.Time;
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

    public List<ReservationTime> findAllTimes() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, createReservationMapper());
    }

    public ReservationTime findTimeById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, createReservationMapper(), id);
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다.");
        }
    }

    public ReservationTime addTime(ReservationTime reservationTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("start_at", Time.valueOf(reservationTime.getStartAt()));

        Number key = jdbcInsert.executeAndReturnKey(param);
        return new ReservationTime(key.longValue(), reservationTime.getStartAt());
    }

    public boolean existTimeByStartAt(LocalTime startAt) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation_time WHERE start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    public void removeTimeById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<ReservationTime> createReservationMapper() {
        return (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime());
    }
}
