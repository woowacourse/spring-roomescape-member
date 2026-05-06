package roomescape.dao;

import java.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Theme> actorRowMapper = (resultSet, rowNum) -> {
        Theme theme = new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));
        return theme;
    };

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme;";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public Theme findBy(Long id) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, actorRowMapper, id);
    }

    public Long insert(Theme theme) {
        String sql = "INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            pstmt.setString(1, theme.getName());
            pstmt.setString(2, theme.getDescription());
            pstmt.setString(3, theme.getThumbnail());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public List<Theme> findPopular(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = "SELECT\n"
                + "    t.id,\n"
                + "    t.name,\n"
                + "    t.description,\n"
                + "    t.thumbnail,\n"
                + "    COUNT(r.id) AS reservation_count\n"
                + "FROM theme AS t\n"
                + "LEFT JOIN reservation AS r\n"
                + "    ON r.theme_id = t.id\n"
                + "    AND r.date >= ?\n"
                + "    AND r.date < ?\n"
                + "GROUP BY t.id, t.name, t.description, t.thumbnail\n"
                + "ORDER BY reservation_count DESC, t.id ASC\n"
                + "LIMIT ?";

        return jdbcTemplate.query(sql, actorRowMapper, startDate, endDate, limit);
    }
}
