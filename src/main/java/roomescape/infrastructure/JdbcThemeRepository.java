package roomescape.infrastructure;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNum) -> {
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String thumbnail = resultSet.getString("thumbnail");
        return Theme.restore(id, name, description, thumbnail);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("theme");
    }

    @Override
    public void save(Theme theme) {
        insert.execute(Map.of(
                "id", theme.getId(),
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()
        ));
    }

    @Override
    public List<Theme> findAll() {
        final String sql = """
                SELECT * FROM theme
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
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
    public Optional<Theme> findById(final String id) {
        final String sql = """
                SELECT * FROM theme
                WHERE id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existById(final String id) {
        final String sql = """
                SELECT COUNT(*) FROM theme
                WHERE id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteById(final String id) {
        final String sql = """
                DELETE FROM theme
                WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);
    }
}
