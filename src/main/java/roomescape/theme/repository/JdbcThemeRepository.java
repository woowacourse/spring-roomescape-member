package roomescape.theme.repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    // TODO: 요구사항에 따라 theme의 runtime은 고정한다.
    private static final Long RUNTIME = 1L;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate,
                               NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                               DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("id");
    }

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            Theme.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail_url"),
                    Duration.ofHours(rs.getLong("runtime"))
            );


    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail_url", theme.getThumbnailUrl())
                .addValue("runtime", RUNTIME);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.toEntity(theme, id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail_url, runtime FROM theme WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);
        try {
            Theme theme = namedParameterJdbcTemplate.queryForObject(sql, params, THEME_ROW_MAPPER);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_url, runtime FROM theme ORDER BY id";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    @Override
    public List<Theme> findPopularThemes(int days, int limit) {
        String sql = """
                 SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail_url,
                    t.runtime
                FROM theme t
                LEFT JOIN reservation r
                    ON t.id = r.theme_id
                WHERE r.date >= DATEADD('DAY', -:days, CURRENT_DATE)
                GROUP BY
                    t.id
                HAVING COUNT(r.id) > 0
                ORDER BY COUNT(r.id) DESC
                LIMIT :limit
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("days", days)
                .addValue("limit", limit);

        return namedParameterJdbcTemplate.query(sql, params, THEME_ROW_MAPPER);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        int affectedRows = namedParameterJdbcTemplate.update(sql, params);
        if (affectedRows == 0) {
            throw new ThemeNotFoundException(id);
        }
    }
}
