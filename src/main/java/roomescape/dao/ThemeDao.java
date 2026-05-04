package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Theme;

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
}
