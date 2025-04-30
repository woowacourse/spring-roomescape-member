package roomescape.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeDaoImpl implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcThemeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("theme")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAllTheme() {
        String query = "select * from theme";
        return jdbcTemplate.query(query,
            (resultSet, RowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
            ));
    }

    @Override
    public void saveTheme(Theme theme) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());
        Number newId = insertActor.executeAndReturnKey(parameters);
        theme.setId(newId.longValue());
    }

    @Override
    public void deleteTheme(Long id) {
        String query = "delete from theme where id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String query = "select * from theme where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
