package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail_url FROM theme";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public Theme findById(long id) {
        String sql = "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), id);
    }

    @Override
    public Theme save(Theme theme) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = Map.of(
                "name", theme.name(),
                "description", theme.description(),
                "thumbnail_url", theme.thumbnailUrl());

        long id = insert.executeAndReturnKey(params).longValue();
        return new Theme(id, theme.name(), theme.description(), theme.thumbnailUrl());
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Theme> findPopularThemes(Long topCount, LocalDate fromDate, LocalDate toDate) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.thumbnail_url,
                    count(*) as reservation_count
                FROM (
                    SELECT *
                    FROM reservation
                    WHERE date BETWEEN ? AND ?
                ) as r
                
                INNER JOIN theme t
                ON r.theme_id = t.id
                GROUP BY t.id
                ORDER BY reservation_count DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, rowMapper(), fromDate, toDate, topCount);
    }

    private RowMapper<Theme> rowMapper() {
        return (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail_url")
        );
    }
}
