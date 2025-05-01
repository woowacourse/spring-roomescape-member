package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

@Repository
public class H2ThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert themeInserter;
    private final RowMapper<Theme> themeMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    public H2ThemeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeInserter = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public boolean isExists(final ThemeName name) {
        final String sql = "SELECT COUNT(*) FROM theme WHERE name = ?";
        long count = jdbcTemplate.queryForObject(sql, Long.class, name.getName());
        return count > 0;
    }

    @Override
    public Theme save(final Theme theme) {
        final Map<String, Object> parameters = Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()
        );
        long id = themeInserter.executeAndReturnKey(parameters).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public List<Theme> findAll() {
        final String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, themeMapper);
    }

    @Override
    public Theme findById(final long id) {
        final String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, themeMapper, id);
    }

    @Override
    public void deleteById(final long id) {
        final String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findPopularThemes(final LocalDate from, final LocalDate to, final int count) {
        final String sql = """
                     SELECT t.id, t.name, t.description, t.thumbnail
                     FROM theme AS t
                     INNER JOIN reservation AS r ON t.id = r.theme_id
                     WHERE r.date >= ? AND r.date <= ?
                     GROUP BY t.id
                     ORDER BY COUNT(*) DESC
                     LIMIT ?
                """;
        return jdbcTemplate.query(sql, themeMapper, from , to, count);
    }

    @Override
    public boolean isNotExistsById(long id) {
        final String sql = "SELECT COUNT(*) FROM theme WHERE id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, id);
        return count == 0;
    }
}
