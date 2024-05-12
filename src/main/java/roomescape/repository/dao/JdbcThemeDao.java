package roomescape.repository.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.theme.Theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long save(Theme theme) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Theme> findAll() {
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
    public Optional<Theme> findById(long id) {
        String sql = "select id, name, description, thumbnail from theme where id = ?";
        Theme theme = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
                new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id);
        return Optional.ofNullable(theme);
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Boolean isExistById(long id) {
        String sql = "select exists(select id from theme where id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean isExistByName(String name) {
        String sql = "select exists (select id from theme where name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }
}
