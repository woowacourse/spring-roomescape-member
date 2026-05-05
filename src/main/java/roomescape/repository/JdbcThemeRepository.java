package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final String FIND_ALL_SQL = """
            SELECT id, name, description, thumbnail
            FROM THEME
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT id, name, description, thumbnail
            FROM theme
            WHERE id = ?
            """;

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, themeRowMapper);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_SQL, themeRowMapper, id)
                .stream()
                .findFirst();
    }
}
