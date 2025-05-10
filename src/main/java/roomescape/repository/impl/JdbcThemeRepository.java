package roomescape.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.mapper.ThemeMapper;
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

    public List<Theme> read() {
        final String query = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(
                query,
                new ThemeMapper()
        );
    }

    public List<Theme> readByAsc(Long listNum) {
        final String query = """
                SELECT t.id, t.name, t.description, t.thumbnail, rc.reservation_count
                FROM (
                    SELECT r.theme_id, COUNT(*) AS reservation_count
                    FROM reservation r
                    WHERE r.date BETWEEN DATEADD('DAY', -7, CURRENT_DATE) AND DATEADD('DAY', -1, CURRENT_DATE)
                    GROUP BY r.theme_id
                    ORDER BY reservation_count ASC
                    LIMIT ?
                ) rc
                INNER JOIN theme t ON rc.theme_id = t.id
                """;

        List<Theme> listedTheme = jdbcTemplate.query(
                query,
                new ThemeMapper(),
                listNum
        );

        return listedTheme;
    }

    public List<Theme> readByDesc(Long listNum) {
        final String query = """
                SELECT t.id, t.name, t.description, t.thumbnail, rc.reservation_count
                FROM (
                    SELECT r.theme_id, COUNT(*) AS reservation_count
                    FROM reservation r
                    WHERE r.date BETWEEN DATEADD('DAY', -7, CURRENT_DATE) AND DATEADD('DAY', -1, CURRENT_DATE)
                    GROUP BY r.theme_id
                    ORDER BY reservation_count DESC
                    LIMIT ?
                ) rc
                INNER JOIN theme t ON rc.theme_id = t.id
                """;

        List<Theme> listedTheme = jdbcTemplate.query(
                query,
                new ThemeMapper(),
                listNum
        );

        return listedTheme;
    }

    public Optional<Theme> findById(Long id) {
        final String query = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            query,
                            new ThemeMapper(),
                            id
                    ));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(Long id) {
        final String query = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}
