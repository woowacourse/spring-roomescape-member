package roomescape.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private static final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme insert(Theme theme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());

        Number generatedId = jdbcInsert.executeAndReturnKey(parameters);
        return new Theme(
                generatedId.longValue(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public Optional<Theme> selectById(Long themeId) {
        String sql = """
                SELECT id, 
                       name, 
                       description,
                       thumbnail
                FROM theme
                WHERE id = ?""";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, ROW_MAPPER, themeId));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }

    public void delete(long themeId) {
        String sql = """
                DELETE FROM theme
                WHERE id = ?""";
        jdbcTemplate.update(sql, themeId);
    }
}
