package roomescape.theme.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNumber) ->
            new Theme(resultSet.getLong("id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(final Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("theme_name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        final Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existsByName(final String name) {
        final String query = "SELECT EXISTS (SELECT 1 FROM theme WHERE theme_name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, name));
    }

    @Override
    public List<Theme> findAll() {
        final String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER);
    }

    @Override
    public Optional<Theme> findById(final long id) {
        final String query = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Theme> findPopularThemes(final LocalDate start, final LocalDate end, final int limit) {

        final String query = """
                        SELECT
                            t.id,
                            t.theme_name,
                            t.description,
                            t.thumbnail
                        FROM theme AS t
                        JOIN reservation AS r
                        ON r.theme_id = t.id
                        WHERE r.date BETWEEN ? AND ?
                        GROUP BY t.id
                        ORDER BY COUNT(r.id) DESC
                        LIMIT ?
                """;
        return jdbcTemplate.query(query, THEME_ROW_MAPPER, start, end, limit);
    }

    @Override
    public boolean deleteById(final long id) {
        final String query = "DELETE FROM theme WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }
}
