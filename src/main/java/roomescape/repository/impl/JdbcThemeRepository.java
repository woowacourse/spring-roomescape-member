package roomescape.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
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
        return jdbcTemplate.query(query, (resultSet, rowNum) ->
                new Theme(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail"))
        );
    }

    public Optional<Theme> findById(Long id) {
        final String query = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, (resultSet, rowNum) ->
                        new Theme(resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getString("description"),
                                resultSet.getString("thumbnail"))
                , id));
    }

    public void delete(Long id) {
        final String query = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}
