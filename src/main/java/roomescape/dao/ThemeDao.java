package roomescape.dao;

import static roomescape.dao.rowMapper.ReservationTimeMapper.RESERVATION_TIME_ROW_MAPPER;
import static roomescape.dao.rowMapper.ThemeMapper.THEME_ROW_MAPPER;

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
                THEME_ROW_MAPPER,
                id
        );
    }

    public List<Theme> findAllThemes() {
        return jdbcTemplate.query(
                "SELECT id, name, description, url FROM theme",
                THEME_ROW_MAPPER
        );
    }

    public List<Theme> findTopThemes(Long count) {
        return jdbcTemplate.query(
                """
                           SELECT
                           t.id, t.name, t.description, t.url, r.reservation_count
                           FROM theme t
                           INNER JOIN (
                               SELECT theme_id, COUNT(id) AS reservation_count
                               FROM reservation
                               WHERE date >= CURRENT_DATE - 7 AND date < CURRENT_DATE
                               GROUP BY theme_id
                           ) r ON t.id = r.theme_id
                           ORDER BY r.reservation_count DESC
                           LIMIT ?;
                        """,
                THEME_ROW_MAPPER,
                count
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
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
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
                RESERVATION_TIME_ROW_MAPPER,
                id, date
        );
    }
}
