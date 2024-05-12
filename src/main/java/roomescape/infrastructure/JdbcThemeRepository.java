package roomescape.infrastructure;

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
import roomescape.domain.theme.ThemeRepository;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private static final int POPULAR_THEMES_SELECTION_DAYS = 7;
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
    public Optional<Theme> findById(long id) {
        String query = """
                SELECT id, name, description, thumbnail FROM theme 
                WHERE id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Theme> findAll() {
        String query = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        int deletedRowCount = jdbcTemplate.update(query, id);
        return deletedRowCount > 0;
    }

    @Override
    public List<Theme> findPopularThemesForWeekLimit10(LocalDate now) {
        LocalDate baseDate = now.minusDays(POPULAR_THEMES_SELECTION_DAYS);
        String query = """
                SELECT t.id, t.name, t.description, t.thumbnail FROM theme AS t
                JOIN (
                    SELECT theme_id, count(*) AS theme_count FROM reservation
                    WHERE reservation_date >= ? AND reservation_date < ?
                    GROUP BY theme_id) AS r
                ON t.id = r.theme_id
                ORDER BY r.theme_count DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(query, ROW_MAPPER, baseDate, now);
    }
}
