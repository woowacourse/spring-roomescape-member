package roomescape.repository.impl;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme save(Theme theme) {
        Map<String, Object> parameters = Map.ofEntries(
                Map.entry("name", theme.getName()),
                Map.entry("description", theme.getDescription()),
                Map.entry("thumbnail", theme.getThumbnail())
        );

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Theme.generateWithPrimaryKey(theme, id);
    }
}
