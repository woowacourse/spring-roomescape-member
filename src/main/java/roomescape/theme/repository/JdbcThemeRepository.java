package roomescape.theme.repository;

import java.sql.PreparedStatement;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    // TODO: 요구사항에 따라 theme의 runtime은 고정한다.
    private static final Long RUNTIME = 1L;
    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> themeRowMapper = (rs, rowNum) ->
            Theme.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail_url"),
                    Duration.ofHours(rs.getLong("runtime"))
            );


    @Override
    public Theme save(String name, String description, String thumbnailUrl) {
        String sql = """
                INSERT INTO theme (name, description, thumbnail_url, runtime)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, thumbnailUrl);
            ps.setLong(4, RUNTIME);
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Theme.of(
                id,
                name,
                description,
                thumbnailUrl,
                Duration.ofHours(RUNTIME)
        );
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT id, name, description, thumbnail_url, runtime FROM theme WHERE id = ?";
        List<Theme> result = jdbcTemplate.query(sql, themeRowMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_url, runtime FROM theme ORDER BY id";
        return jdbcTemplate.query(sql, themeRowMapper);
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
                WHERE r.date >= DATEADD('DAY', -?, CURRENT_DATE)
                GROUP BY
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail_url,
                    t.runtime
                HAVING COUNT(r.id) > 0
                ORDER BY COUNT(r.id) DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, themeRowMapper, days, limit);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";

        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new ThemeNotFoundException(id);
        }
    }
}
