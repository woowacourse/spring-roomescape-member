package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final RowMapper<Theme> ROW_MAPPER =
            (resultSet, rowNum) -> {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String thumbnail = resultSet.getString("thumbnail");
                return Theme.afterSave(id, name, description, thumbnail);
            };

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Theme save(Theme theme) {
        final String sql = """
                INSERT INTO theme (name, description, thumbnail)
                VALUES (?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        return Theme.afterSave(keyHolder.getKey().longValue(), theme);
    }

    @Override
    public List<Theme> findAll() {
        final String sql = """
                SELECT * FROM theme
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean existById(long id) {
        final String sql = """
                SELECT COUNT(*) FROM theme
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Theme findById(long id) {
        try {
            final String sql = """
                    SELECT * FROM theme
                    WHERE id = ?
                    """;
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int count) {
        final String sql = """
                SELECT t.*
                FROM (
                    SELECT r.theme_id, COUNT(*) AS reservation_count
                    FROM reservation r
                    WHERE r.date BETWEEN ? AND ?
                    GROUP BY r.theme_id
                    ORDER BY reservation_count DESC
                    LIMIT ?
                ) AS ranked
                JOIN theme t ON ranked.theme_id = t.id;
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER, startInclusive, endInclusive, count);
    }

    @Override
    public void deleteById(long id) {
        final String sql = """
                DELETE FROM theme
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }
}
