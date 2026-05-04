package roomescape.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeDaoData;

@Repository
public class ThemeDao {
    private static final String SELECT_ALL_SQL = "SELECT id, name, description, image_url FROM theme";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM theme WHERE id = ?";
    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, name, description, image_url FROM theme WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public ThemeDaoData addTheme(ThemeCommand themeCommand) {
        long id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "name", themeCommand.name(),
                "description", themeCommand.description(),
                "image_url", themeCommand.imageUrl()
        )).longValue();

        return ThemeDaoData.from(id, themeCommand);
    }

    public List<ThemeDaoData> getAllTheme() {
        return jdbcTemplate.query(SELECT_ALL_SQL, (rs, i) -> ThemeDaoData.from(rs));
    }

    public void deleteTheme(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    public Optional<ThemeDaoData> getTheme(long id) {
        return jdbcTemplate.query(SELECT_SPECIFIC_ID_SQL, ((rs, rowNum) -> ThemeDaoData.from(rs)), id)
                .stream()
                .findFirst();
    }
}
