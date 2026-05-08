package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.dto.PopularTheme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("imgUrl")
    );

    private RowMapper<PopularTheme> popularThemeRowMapper = (resultSet, rowNum) -> new PopularTheme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("imgUrl"),
            resultSet.getLong("theme_rank"),
            resultSet.getLong("reservation_count")
    );

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme findById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
    }

    public List<Theme> findAllThemes() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
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
        return jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    public List<PopularTheme> findPopularThemes(LocalDate from, LocalDate to) {
        String sql = """
                SELECT
                    ranked.theme_rank,
                    ranked.id,
                    ranked.name,
                    ranked.description,
                    ranked.imgUrl,
                    ranked.reservation_count
                FROM (
                    SELECT
                        ROW_NUMBER() OVER (ORDER BY COUNT(r.id) DESC, t.id ASC) AS theme_rank,
                        t.id,
                        t.name,
                        t.description,
                        t.imgUrl,
                        COUNT(r.id) AS reservation_count
                    FROM theme t
                    JOIN reservation r ON t.id = r.theme_id
                    WHERE r.date BETWEEN ? AND ?
                    GROUP BY t.id, t.name, t.description, t.imgUrl
                ) ranked
                WHERE ranked.theme_rank <= 10
                ORDER BY ranked.theme_rank;
                """;
        return jdbcTemplate.query(sql, popularThemeRowMapper, from.toString(),
                to.toString());
    }
}
