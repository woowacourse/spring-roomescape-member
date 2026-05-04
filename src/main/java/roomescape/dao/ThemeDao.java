package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;

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
}
