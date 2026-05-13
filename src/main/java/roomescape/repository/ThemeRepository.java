package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeRepository {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = ((rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("url")
    ));

    private final JdbcTemplate jdbcTemplate;

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

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM THEME WHERE ID = ?";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM THEME";
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    public List<Theme> findByCurrentDateAndLastWeekDateAndLimit(LocalDate endDate, LocalDate startDate,
                                                                int limit) {
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
                ORDER BY (COUNT(r.id)) DESC , t.id ASC
                LIMIT ?;
                """;
        return jdbcTemplate.query(sql, THEME_ROW_MAPPER, startDate, endDate, limit);
    }
}
