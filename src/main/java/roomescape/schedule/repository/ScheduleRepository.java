package roomescape.schedule.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ScheduleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Schedule> findAll(Long themeId, LocalDate date) {
        String sql = """
                SELECT s.id AS schedule_id, 
                       s.start_at, 
                       s.end_at, 
                       t.id AS theme_id, 
                       t.name AS theme_name,
                       t.description,
                       t.image_url
                FROM schedule s
                INNER JOIN theme t ON s.theme_id = t.id
                LEFT JOIN reservation r ON s.id = r.schedule_id
                WHERE s.theme_id = ?
                  AND s.start_at >= ? 
                  AND s.start_at < ?
                """;

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Theme theme = new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("description"), resultSet.getString("image_url"));
            return new Schedule(
                    resultSet.getLong("schedule_id"),
                    resultSet.getObject("start_at", LocalDateTime.class),
                    resultSet.getObject("end_at", LocalDateTime.class),
                    theme
            );
        }, themeId, startOfDay, endOfDay);
    }
}
