package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeRepository {

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            new Theme(rs.getLong("id"), rs.getString("name"), rs.getString("description"), rs.getString("thumbnail_url"));

    private final JdbcTemplate jdbcTemplate;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // save

    // findById
    public Theme findById(long themeId){
        String sql = "SELECT id, name, description, thumbnail_url FROM THEME WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, THEME_ROW_MAPPER, themeId);
    }
}
