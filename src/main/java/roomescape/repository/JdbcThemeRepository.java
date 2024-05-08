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
public class JdbcThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Theme findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        Theme theme = jdbcTemplate.queryForObject(sql, themeRowMapper, id);
        if (theme == null) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다");
        }

        return theme;
    }

    public List<Theme> findTopThemesWithinDays(LocalDate date, int limit) {
        String sql = "SELECT "
                + "    th.id AS theme_id, "
                + "    th.name AS theme_name, "
                + "    th.description AS theme_description, "
                + "    th.thumbnail AS theme_thumbnail, "
                + "    COUNT(r.id) AS total_reservations "
                + "FROM theme AS th "
                + "INNER JOIN reservation AS r ON r.theme_id = th.id "
                + "WHERE r.date > ? "
                + "GROUP BY th.id "
                + "ORDER BY total_reservations DESC "
                + "LIMIT ?";
        return jdbcTemplate.query(sql, themeRowMapper, date, limit);
    }

    public Theme save(Theme theme) {
        Map<String, String> params = Map.of(
                "name", theme.getName(),
                "description", theme.getDescription(),
                "thumbnail", theme.getThumbnail()
        );
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
