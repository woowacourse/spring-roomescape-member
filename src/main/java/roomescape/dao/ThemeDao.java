package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> {
        Theme theme = new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("imgUrl")
        );
        return theme;
    };

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
    }

    public List<Theme> findAllThemes() {
        String sql = "SELECT * FROM theme";
        List<Theme> themes = jdbcTemplate.query(
                sql,
                themeRowMapper
        );
        return themes;
    }

    public Long insertTheme(String name, String description, String imgUrl) {
        String sql = "INSERT INTO theme (name, description, imgUrl) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    sql, new String[]{"id"}
            );
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, imgUrl);
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        return jdbcTemplate.update("delete from theme where id = ?", id);
    }

    public List<Theme> findPopularThemes(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.imgUrl
                FROM theme t
                JOIN reservation r ON t.id = r.theme_id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id, t.name, t.description, t.imgUrl
                ORDER BY COUNT(r.id) DESC
                LIMIT 10
                """;
        List<Theme> themes = jdbcTemplate.query(sql, themeRowMapper, from.toString(), to.toString());
        return themes;
    }
}
