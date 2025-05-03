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
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String thumbnail = resultSet.getString("thumbnail");
        return Theme.afterSave(id, name, description, thumbnail);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        final Number id = insert.executeAndReturnKey(Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()
        ));

        return Theme.afterSave(
                id.longValue(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
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
    public Optional<Theme> findById(long id) {
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
