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
import roomescape.domain.Limit;
import roomescape.domain.Theme;
import roomescape.domain.VisitDate;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ThemeRowMapper rowMapper;

    public ThemeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final ThemeRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Theme create(final Theme theme) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.name())
                .addValue("description", theme.description())
                .addValue("thumbnail", theme.getThumbnailAsString());

        final long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.of(id, theme.name(), theme.description(), theme.getThumbnailAsString());
    }

    public Optional<Theme> findById(final long id) {
        final String sql = "SELECT * FROM theme WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Theme> findPopular(final VisitDate visitDate, final Limit limit) {
        final String sql = """
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
                LIMIT ?;
                """;
        return jdbcTemplate.query(sql, rowMapper, visitDate.beforeWeek().asString(), visitDate.beforeDay().asString(), limit.value());
    }

    public List<Theme> getAll() {
        final String sql = "SELECT id, name,description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void delete(final long id) {
        final String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
