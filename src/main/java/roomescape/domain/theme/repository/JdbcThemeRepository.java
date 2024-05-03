package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private static final int DATE_RANGE = 7;
    private static final RowMapper<Theme> ROW_MAPPER = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations simpleJdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean existsByName(String name) {
        String query = """
                SELECT EXISTS(
                    SELECT 1
                    FROM theme
                    WHERE name = ?
                )
                """;
        return jdbcTemplate.queryForObject(query, Boolean.class, name);
    }

    @Override
    public Optional<Theme> findById(long id) {
        String query = "SELECT * FROM theme WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public void deleteById(long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    public List<Theme> findPopularThemes(LocalDate now) {
        LocalDate prev = now.minusDays(DATE_RANGE);
        String query = """
                SELECT * FROM theme AS t
                JOIN (
                    SELECT theme_id, count(*) AS theme_count, reservation_date FROM reservation
                    GROUP BY theme_id) AS r
                ON t.id = r.theme_id
                WHERE r.reservation_date >= ? AND r.reservation_date < ?
                ORDER BY r.theme_count DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(query, ROW_MAPPER, prev, now);
    }
}
