package roomescape.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert;
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
}
