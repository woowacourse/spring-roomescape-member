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

    private final RowMapper<Long> idRowMapper = (resultSet, rowNum)
        -> resultSet.getLong("theme_id");

    private final RowMapper<Theme> rowMapper = (resultSet, rowNum) -> Theme.of(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getString("description"),
        resultSet.getString("image_url")
    );

    public List<Long> findThemeIdTop10(LocalDate startDate, LocalDate endDate) {
        String query = """
            SELECT r.theme_id AS theme_id
            FROM reservation r
            WHERE r.date BETWEEN ? AND ?
            GROUP BY r.theme_id
            ORDER BY COUNT(r.id) DESC
            LIMIT 10
            """;

        return jdbcTemplate.query(query, idRowMapper, startDate, endDate);
    }

    public Theme findById(Long id) {
        String query = "select * from theme where id = ?";
        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }
}
