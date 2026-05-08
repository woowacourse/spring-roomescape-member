package roomescape.user.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.admin.domain.Theme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.of(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("description"),
        resultSet.getString("image_url")
    );

    public List<Theme> findTop10ByDateBetween(LocalDate startDate, LocalDate endDate) {
        String query = """
            SELECT t.id, t.name, t.description, t.image_url
            FROM theme t
            INNER JOIN reservation r ON t.id = r.theme_id
            WHERE r.date BETWEEN ? AND ?
            GROUP BY t.id, t.name, t.description, t.image_url
            ORDER BY COUNT(r.id) DESC
            LIMIT 10
            """;
        return jdbcTemplate.query(query, rowMapper, startDate, endDate);
    }
}
