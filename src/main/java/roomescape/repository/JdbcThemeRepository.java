package roomescape.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<Theme> rowMapper = ((rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    ));

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT * FROM theme", rowMapper);
    }

    @Override
    public Theme insert(Theme theme) {
        Map<String, Object> themeRow = new HashMap<>();
        themeRow.put("name", theme.getName());
        themeRow.put("description", theme.getDescription());
        themeRow.put("thumbnail", theme.getThumbnail());
        Long id = simpleJdbcInsert.executeAndReturnKey(themeRow).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findThemeOrderByReservationCount() {
        String sql = """
                SELECT id, name, description, thumbnail
                COUNT(id) AS reservation_count
                FROM theme AS th
                INNER JOIN reservation AS r
                ON id = r.theme_id
                WHERE r.date BETWEEN DATEADD('day', -7, CURRENT_DATE()) AND DATEADD('day', -1, CURRENT_DATE())
                GROUP BY th.id
                ORDER BY reservation_count DESC 
                LIMIT 10
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }
}
