package roomescape.user.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Schedule;

@Repository
public class ScheduleDao {
    private static final RowMapper<Schedule> rowMapper = (rs, rowNum) -> {
        return new Schedule(
                rs.getLong("id"),
                rs.getLong("theme_id"),
                LocalDate.parse(rs.getString("date")),
                LocalTime.parse(rs.getString("start_at")),
                rs.getBoolean("is_available")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public ScheduleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Schedule> selectById(Long themeId, LocalDate date) {
        String sql = "SELECT * FROM schedule Where theme_id = ? AND date = ?";
        return jdbcTemplate.query(sql, rowMapper, themeId, date);
    }
}
