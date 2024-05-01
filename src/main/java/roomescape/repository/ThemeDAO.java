package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ThemeDAO implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertActor;

    public ThemeDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAllThemes() {
        String sql = "select id, name, description, thumbnail from theme";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ));
    }

    @Override
    public Theme addTheme(Theme theme) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return new Theme(newId.longValue(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void deleteTheme(long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Theme findThemeById(long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, ignored) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id);
    }
}
