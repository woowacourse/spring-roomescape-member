package roomescape.dao;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeDaoData;

@Repository
public class ThemeDao {
    private static final String SELECT_ALL_SQL = "SELECT id, name, description, image_url FROM theme";

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
}
