package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public Theme save(Theme theme) {
        String sql = "INSERT INTO theme(name, description, url) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setObject(2, theme.getDescription());
            ps.setObject(3, theme.getUrl());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getUrl());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM THEME WHERE ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "SELECT * FROM THEME WHERE ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, themeRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
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
