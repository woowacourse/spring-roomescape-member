package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (selectedTheme, rowNum) -> new Theme(
            selectedTheme.getLong("id"),
            selectedTheme.getString("name"),
            selectedTheme.getString("description"),
            selectedTheme.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert themeInsert;

    public ThemeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.themeInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme save(final Theme theme) {
        final BeanPropertySqlParameterSource themeParameters = new BeanPropertySqlParameterSource(theme);
        final Long savedThemeId = themeInsert.executeAndReturnKey(themeParameters).longValue();
        return new Theme(savedThemeId, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public List<Theme> findAll() {
        final String selectQuery = "SELECT * FROM theme";
        return jdbcTemplate.query(selectQuery, THEME_ROW_MAPPER);
    }

    public Optional<Theme> findById(final Long id) {
        final String selectQuery = "SELECT * FROM theme WHERE id = ? LIMIT 1";
        try {
            final Theme theme = jdbcTemplate.queryForObject(selectQuery, THEME_ROW_MAPPER, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
