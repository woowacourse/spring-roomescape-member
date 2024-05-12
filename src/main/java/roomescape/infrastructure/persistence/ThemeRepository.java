package roomescape.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Description;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public void removeById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    public List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, int limitCount) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail
                FROM theme as t
                LEFT JOIN reservation AS r
                ON t.id = r.theme_id
                AND convert(r.date, DATE) BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY count(r.id) DESC, t.id ASC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, getThemeRowMapper(), startDate, endDate, limitCount);
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail FROM theme", getThemeRowMapper());
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ? LIMIT 1";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, getThemeRowMapper(), id));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                new ThemeName(resultSet.getString("name")),
                new Description(resultSet.getString("description")),
                resultSet.getString("thumbnail"));
    }
}
