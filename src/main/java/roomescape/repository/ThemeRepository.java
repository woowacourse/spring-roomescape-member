package roomescape.repository;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeRepository {

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            Theme.of(rs.getLong("id"), rs.getString("name"), rs.getString("description"),
                    rs.getString("thumbnail_url"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public void deleteById(long themeId) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, themeId);
    }

    public Theme save(Theme theme) {
        Map<String, Object> params = Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail_url", theme.getThumbnailUrl()
        );
        long generatedKey = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.of(generatedKey, theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
    }

    public Theme findById(long themeId) {
        String sql = "SELECT id, name, description, thumbnail_url FROM THEME WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, THEME_ROW_MAPPER, themeId);
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_url FROM THEME";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }
}
