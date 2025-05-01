package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

@Repository
public class H2ThemeRepository implements ThemeRepository{

    private static final RowMapper<Theme> mapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertTheme;

    static {
        mapper = (resultSet, resultNumber) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    public H2ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertTheme = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, mapper);
    }

    public Optional<Theme> findById(long id) {
        String sql = "SELECT * FROM theme WHERE theme.id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(sql, mapper, id);
            return Optional.of(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public long addTheme(Theme theme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", theme.getName());
        parameters.put("description", theme.getDescription());
        parameters.put("thumbnail", theme.getThumbnail());

        return insertTheme.executeAndReturnKey(parameters).longValue();
    }

    public void deleteById(long id) {
        String sql = "DELETE FROM theme where theme.id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Theme> getTopThemesByCount(LocalDate startDate, LocalDate endDate) {
        String sql =
            """
            SELECT t.id, t.name, t.description, t.thumbnail
            FROM theme AS t
            ORDER BY (
                SELECT COUNT(*)
                FROM reservation AS r
                WHERE r.theme_id = t.id 
                AND r.date >= ? AND r.date <= ?
            ) DESC 
            LIMIT 10;
            """;

        return jdbcTemplate.query(sql, mapper, startDate, endDate);
    }
}