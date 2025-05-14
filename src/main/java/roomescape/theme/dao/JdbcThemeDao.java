package roomescape.theme.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeMapper = (resultSet, rowNum) -> Theme.load(
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
        final String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, themeMapper);
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return jdbcTemplate.query(sql, themeMapper, id).stream().findFirst();
    }

    @Override
    public Theme create(final Theme theme) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>(Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()));

        Number key = jdbcInsert.executeAndReturnKey(parameters);
        return Theme.load(key.longValue(), theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void delete(Theme theme) {
        final String sql = "DELETE FROM theme t WHERE t.id = ?";
        jdbcTemplate.update(sql, theme.getId());
    }
}
