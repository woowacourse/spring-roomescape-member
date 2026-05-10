package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Theme> themeRowMapper = ((rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("url")
    ));

    public ThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(String name, String description, String url) {
        String sql = "INSERT INTO theme(name, description, url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setObject(2, description);
            ps.setObject(3, url);
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        return new Theme(id, name, description, url);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM THEME WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    public Theme findById(Long id) {
        String sql = "SELECT * FROM THEME WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM THEME";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public List<Theme> findByCurrentDateAndLastWeekDateAndLimit(String currentDate, String lastWeekDate, int limit) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.url
                FROM theme t
                INNER JOIN reservation r ON r.theme_id = t.id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id
                order by (COUNT(r.id)) desc, t.id asc
                limit ?;
                """;
        return jdbcTemplate.query(sql, themeRowMapper, lastWeekDate, currentDate, limit);
    }
}
