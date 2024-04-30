package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail"));

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
        final String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    public Optional<Theme> findById(final Long id) {
        final String sql = "SELECT * FROM theme WHERE id = ?";
        try {
            final Theme theme = jdbcTemplate.queryForObject(sql, THEME_ROW_MAPPER, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
