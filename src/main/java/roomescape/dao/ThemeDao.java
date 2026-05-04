package roomescape.dao;

import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeDaoData;

@Repository
public class ThemeDao {

    private static final String INSERT_SQL = "INSERT INTO theme (name, description, image_url) VALUES (?, ?, ?)";

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
}
