package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private static final RowMapper<Theme> THEME_ROW_MAPPER =
            (resultSet, rowNum) -> new Theme(
                    resultSet.getLong("id"),
                    ThemeName.from(resultSet.getString("name")),
                    ThemeDescription.from(resultSet.getString("description")),
                    ThemeThumbnail.from(resultSet.getString("thumbnail"))
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcThemeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> readAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    @Override
    public Optional<Theme> readById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ? ";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, THEME_ROW_MAPPER, id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Theme create(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName().getValue())
                .addValue("description", theme.getDescription().getValue())
                .addValue("thumbnail", theme.getThumbnail().getValue());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Theme(id, theme);
    }

    @Override
    public boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM theme WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    @Override
    public void delete(long id) {
        String sql = """
                DELETE
                FROM theme
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
