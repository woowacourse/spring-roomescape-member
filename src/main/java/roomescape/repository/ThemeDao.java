package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.exception.IllegalThemeException;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Theme findById(long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalThemeException("[ERROR] 테마를 찾을 수 없습니다");
        }
    }

    public List<Theme> findAll() {
        List<Theme> themes = jdbcTemplate.query("SELECT * FROM theme", themeRowMapper);
        return Collections.unmodifiableList(themes);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    public List<Theme> findThemesByDescOrder() {
        String nowDate = LocalDate.now().toString();
        String weekBeforeDate = LocalDate.now().minusDays(7).toString();
        return jdbcTemplate.query("""
                SELECT
                th.id as theme_id,
                th.name,
                th.description,
                th.thumbnail,
                COUNT(r.theme_id) AS reservation_count
                FROM theme AS th
                INNER JOIN reservation AS r ON r.theme_id = th.id
                WHERE r.date < ? AND r.date > ?
                GROUP BY th.id, th.name, th.description, th.thumbnail
                ORDER BY reservation_count DESC
                LIMIT 10
                """, themeRowMapper, nowDate, weekBeforeDate);
    }
}
