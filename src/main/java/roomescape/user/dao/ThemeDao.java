package roomescape.user.dao;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {

    private static final RowMapper<Theme> rowMapper =
            (rs, rowNum) -> {
                return new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"));
            };

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> selectAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Theme selectById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Theme> selectByTrend(LocalDate from, LocalDate to, int limit) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.image,
                    COUNT(r.id) AS reservation_count
                FROM theme t
                JOIN reservation r ON r.theme_id = t.id
                WHERE r.date >= ?
                    AND r.date < ?
                GROUP BY t.id, t.name, t.description, t.image
                ORDER BY reservation_count DESC
                LIMIT ?;
                """;

        return jdbcTemplate.query(sql, rowMapper, from, to, limit);
    }
}
