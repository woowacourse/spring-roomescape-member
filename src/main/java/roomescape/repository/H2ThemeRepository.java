package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeDescription;
import roomescape.domain.ThemeName;

import java.util.List;
import java.util.Optional;

@Repository
public class H2ThemeRepository implements ThemeRepository {
    private final NamedParameterJdbcTemplate template;

    public H2ThemeRepository(final NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Theme> findById(final Long themeId) {
        String sql = "SELECT * FROM theme WHERE id = :id";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", themeId);
            Theme theme = template.queryForObject(sql, param, itemRowMapper());

            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Theme> itemRowMapper() {
        return ((rs, rowNum) -> new Theme(
                rs.getLong("id"),
                new ThemeName(rs.getString("name")),
                new ThemeDescription(rs.getString("description")),
                rs.getString("thumbnail")
        ));
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return template.query(sql, itemRowMapper());
    }

    @Override
    public Theme save(final Theme theme) {
        String sql = "INSERT INTO theme(name, description, thumbnail) VALUES(:name, :description, :thumbnail)";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", theme.getName().getValue())
                .addValue("description", theme.getDescription().getValue())
                .addValue("thumbnail", theme.getThumbnail());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long savedThemeId = keyHolder.getKey().longValue();

        return theme.initializeIndex(savedThemeId);
    }
}
