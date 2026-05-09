package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.domain.ThemeSortType;

@Repository
public class ThemeJdbcTemplateRepository implements ThemeRepository {

    private static final String FIND_QUERY = "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT id, name, description, thumbnail_url FROM theme";
    private static final String FIND_TOP_N_BY_PERIOD_QUERY = """
            SELECT
                t.id,
                t.name,
                t.description,
                t.thumbnail_url
            FROM theme t
            JOIN reservation r ON r.theme_id = t.id
            WHERE r.date BETWEEN ? AND ?
            GROUP BY t.id, t.name
            ORDER BY %s
            LIMIT ?
            """;
    private static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail_url")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        Map<String, Object> params = prepareInsertParams(theme);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return theme.appendId(id);
    }

    private Map<String, Object> prepareInsertParams(Theme theme) {
        return Map.of(
                "name", theme.name(),
                "description", theme.description(),
                "thumbnail_url", theme.thumbnailUrl()

        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        List<Theme> findTheme = jdbcTemplate.query(FIND_QUERY,
                THEME_ROW_MAPPER,
                id
        );
        return findTheme.stream()
                .findFirst();
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, THEME_ROW_MAPPER);
    }

    @Override
    public List<Theme> findTopNByPeriod(LocalDate startAt, LocalDate endAt, ThemeSortType sortType, Long limit) {
        return jdbcTemplate.query(
                FIND_TOP_N_BY_PERIOD_QUERY.formatted(sortType.getSql()),
                THEME_ROW_MAPPER,
                startAt,
                endAt,
                limit
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
