package roomescape.dao.theme;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public JdbcThemeDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        final String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(sql, themeMapper);
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = """
                SELECT id, name, description, thumbnail
                FROM theme WHERE id = ?
                """;
        return jdbcTemplate.query(sql, themeMapper, id).stream().findFirst();
    }

    @Override
    public Theme create(final Theme theme) {
        final SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>(Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()));

        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return new Theme(key.longValue(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteIfNoReservation(final long id) {
        final String sql = """
                DELETE FROM theme t 
                WHERE t.id = ? 
                AND NOT EXISTS (
                    SELECT 1 
                    FROM reservation r 
                    WHERE r.theme_id = t.id
                )
                """;
        return jdbcTemplate.update(sql, id) == 1;
    }

    @Override
    public List<Theme> findPopularThemesInRecentSevenDays(final LocalDate startDate, final LocalDate endDate) {
        final String sql = """
                SELECT th.id, th.name, th.description, th.thumbnail
                FROM theme th
                INNER JOIN (
                    SELECT theme_id, COUNT(*) AS cnt
                    FROM reservation as rs
                    WHERE rs.date BETWEEN ? AND ?
                    GROUP BY theme_id
                    ORDER BY cnt DESC
                    LIMIT 10
                ) r ON th.id = r.theme_id;
                """;
        return jdbcTemplate.query(sql, themeMapper, startDate, endDate);
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = """
                SELECT COUNT(*)
                FROM theme
                WHERE id = ?
                """;
        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
