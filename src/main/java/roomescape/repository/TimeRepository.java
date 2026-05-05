package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

@Repository
public class TimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public TimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static RowMapper<ReservationTime> timeRowMapper = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getObject("start_at", LocalTime.class)
    );

    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";
        return jdbcTemplate.query(sql, timeRowMapper);
    }

    public List<ReservationTime> findAllByThemeIdAndDate(Long themeId, String date) {
        String sql =
                // [덩어리 1] 우리 매장의 모든 운영 시간 (전체 집합)
                "SELECT id, start_at FROM reservation_time " +
                        "EXCEPT " +
                        // [덩어리 2] 특정 날짜/테마에 이미 누군가 예약한 시간 (차집합)
                        "SELECT t.id, t.start_at FROM reservation r " +
                        "JOIN reservation_time t ON r.time_id = t.id " +
                        "WHERE r.date = ? AND r.theme_id = ?";
        return jdbcTemplate.query(sql, timeRowMapper, date, themeId);
    }

    public void removeById(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public ReservationTime saveTime(LocalTime startAt) {
        String sql = "insert into reservation_time(start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setObject(1, startAt);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationTime(id, startAt);
    }

    public ReservationTime selectById(Long id) {
        String sql = "select * from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql, timeRowMapper, id);
    }
}
