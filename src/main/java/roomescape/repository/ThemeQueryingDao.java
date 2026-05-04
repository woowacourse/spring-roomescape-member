package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeQueryingDao {

    private final JdbcTemplate jdbcTemplate;

    public ThemeQueryingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> {
        Theme theme = new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("url")
        );
        return theme;
    };

    public Optional<Theme> findThemeById(long id) {
        String sql = """
                SELECT id, name, description, url
                FROM theme
                WHERE id = ?
                """;

        try {
            Theme theme = jdbcTemplate.queryForObject(sql, themeRowMapper, id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<Theme> findAllTheme() {
        String sql = """
                SELECT id, name, description, url
                FROM theme
                """;
        return jdbcTemplate.query(sql, themeRowMapper);
    }
}
