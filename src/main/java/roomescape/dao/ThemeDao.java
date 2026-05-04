package roomescape.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme findThemeById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, name, description, url FROM theme WHERE id = ?",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("url")
                ),
                id
        );
    }

    public Theme save(Theme theme) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", theme.getName());
        params.put("description", theme.getDescription());
        params.put("url", theme.getUrl());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(
                id,
                theme.getName(),
                theme.getDescription(),
                theme.getUrl()
        );
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    public List<ReservationTime> findAvailableTime(Long id, String date) {
        return jdbcTemplate.query(
                """
                        SELECT t.id AS time_id, t.start_at
                        FROM reservation_time t
                        LEFT JOIN reservation r ON t.id = r.time_id
                           AND r.theme_id = ?
                           AND r.date = ?
                        WHERE r.id is NULL
                   """,
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getTime("start_at").toLocalTime()
                ),
                id, date
        );
    }
}
