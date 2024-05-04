package roomescape.theme.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeJdbcDao implements ThemeDao{

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeJdbcDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(theme);
        long id = jdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        theme.setId(id);
        return theme;
    }

    @Override
    public Theme findById(long themeId) {
        String findThemeSql = """
                SELECT id, name, description, thumbnail
                FROM theme
                WHERE theme.id = ?
                """;

        return jdbcTemplate.queryForObject(findThemeSql, THEME_ROW_MAPPER, themeId);
    }

    @Override
    public List<Theme> findAllThemes() {
        String findAllThemesSql = "SELECT id, name, description, thumbnail FROM theme";

        return jdbcTemplate.query(findAllThemesSql, THEME_ROW_MAPPER);
    }

    @Override
    public void deleteById(long themeId) {
        String deleteThemeSql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(deleteThemeSql, themeId);
    }
}
