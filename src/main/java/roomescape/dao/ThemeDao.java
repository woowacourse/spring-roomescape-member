package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.ThemeRowMapper;
import roomescape.domain.Theme;
import roomescape.domain.VisitDate;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ThemeRowMapper rowMapper;

    public ThemeDao(JdbcTemplate jdbcTemplate, DataSource dataSource, ThemeRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Theme create(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnailAsString());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getThumbnailAsString());
    }

    public Optional<Theme> find(long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Theme> getPopularTheme(VisitDate visitDate) {
        String sql = """
                SELECT
                    t.id AS theme_id,
                    t.name AS theme_name,
                    t.description AS theme_description,
                    t.thumbnail AS theme_thumbnail
                FROM theme AS t
                INNER JOIN reservation AS r ON r.theme_id = t.id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY COUNT(r.id) DESC
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql, rowMapper, visitDate.beforeWeek().asString(), visitDate.beforeDay().asString());
    }

    public List<Theme> getAll() {
        String sql = "SELECT id, name,description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
