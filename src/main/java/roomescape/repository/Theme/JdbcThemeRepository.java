package roomescape.repository.Theme;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme.PopularThemeCondition;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeWithCount;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private static final String TABLE_NAME = "reservation_theme";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";

    private static final String SELECT_ALL_SQL = "SELECT id, name, description, image_url FROM reservation_theme";
    private static final String DELETE_SPECIFIC_ID_SQL = "DELETE FROM reservation_theme WHERE id = ?";
    private static final String SELECT_SPECIFIC_ID_SQL = "SELECT id, name, description, image_url FROM reservation_theme WHERE id = ?";

    private static final String SELECT_POPULAR_THEMES_BY_DATE_RANGE = """
        SELECT t.id AS id, t.name AS name, t.description AS description, t.image_url AS image_url, reservation_count AS count
        FROM reservation_theme t
        JOIN (
            SELECT theme_id, COUNT(id) AS reservation_count
            FROM reservation
            WHERE created_at BETWEEN ? AND ?
            GROUP BY theme_id
        ) AS r ON r.theme_id = t.id
        ORDER BY r.reservation_count DESC
        LIMIT ?
    """;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(COLUMN_ID);    }

    public Theme addTheme(ThemeCommand themeCommand) {
        long id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                COLUMN_NAME, themeCommand.name(),
                COLUMN_DESCRIPTION, themeCommand.description(),
                COLUMN_IMAGE_URL, themeCommand.imageUrl()
        )).longValue();

        return Theme.from(id, themeCommand);
    }

    public List<Theme> getAllTheme() {
        return jdbcTemplate.query(SELECT_ALL_SQL, (rs, i) -> Theme.from(rs));
    }

    public Optional<Theme> getTheme(long id) {
        return jdbcTemplate.query(SELECT_SPECIFIC_ID_SQL, ((rs, rowNum) -> Theme.from(rs)), id)
                .stream()
                .findFirst();
    }

    public void deleteTheme(long id) {
        jdbcTemplate.update(DELETE_SPECIFIC_ID_SQL, id);
    }

    @Override
    public List<ThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition) {
        return jdbcTemplate.query(SELECT_POPULAR_THEMES_BY_DATE_RANGE, (rs, i) -> ThemeWithCount.from(rs),
                popularThemeCondition.startDate(),
                popularThemeCondition.endDate(),
                popularThemeCondition.size()
        );
    }
}
