package roomescape.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Theme> readAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme" ;
        return jdbcTemplate.query(sql, themeRowMapper());
    }

    @Override
    public Optional<Theme> readById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ? " ;
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, themeRowMapper(), id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Theme create(Theme theme) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("theme").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", theme.getName().getValue());
        params.put("description", theme.getDescription().getValue());
        params.put("thumbnail", theme.getThumbnail().getValue());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public Boolean exist(long id) {
        String sql = """
                SELECT
                CASE
                    WHEN EXISTS (SELECT 1 FROM theme WHERE id = ?)
                    THEN TRUE
                    ELSE FALSE
                END
                """;
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
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

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                ThemeName.from(resultSet.getString("name")),
                ThemeDescription.from(resultSet.getString("description")),
                ThemeThumbnail.from(resultSet.getString("thumbnail"))
        );
    }
}
