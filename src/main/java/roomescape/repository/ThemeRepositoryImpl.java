package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            Theme.of(rs.getLong("id"), rs.getString("name"), rs.getString("description"),
                    rs.getString("thumbnail_url"));
    private static final String EXISTS_BY_ID = """
            SELECT EXISTS (
                SELECT 1 
                    FROM theme
                    WHERE id = ?
                    )
            """;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public void deleteById(long themeId) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, themeId);
    }

    @Override
    public boolean existsById(long themeId) {
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(EXISTS_BY_ID, Boolean.class, themeId));
    }

    @Override
    public Theme save(Theme theme) {
        Map<String, Object> params = Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail_url", theme.getThumbnailUrl()
        );
        long generatedKey = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.of(generatedKey, theme.getName(), theme.getDescription(), theme.getThumbnailUrl());
    }

    @Override
    public Optional<Theme> findById(long themeId) {
        String sql = "SELECT id, name, description, thumbnail_url FROM THEME WHERE id = ?";
        List<Theme> result = jdbcTemplate.query(sql, THEME_ROW_MAPPER, themeId);
        return result.stream().findFirst();
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_url FROM THEME";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    @Override
    public List<Theme> findFamous(long days, LocalDate date, long limit) {
        LocalDate startDate = date.minusDays(days);
        LocalDate endDate = date.minusDays(1);

        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail_url
                FROM THEME AS t
                INNER JOIN (
                    SELECT theme_id, count(theme_id) AS cnt
                    FROM RESERVATION
                    WHERE date BETWEEN ? AND ?
                    GROUP BY theme_id
                    ORDER BY count(theme_id) DESC, theme_id DESC
                    LIMIT ?
                ) AS topN ON t.id = topN.theme_id
                ORDER BY topN.cnt DESC, topN.theme_id DESC
                """;

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER, startDate, endDate, limit);
    }
}
