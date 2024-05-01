package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Theme> rowMapper;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    public List<Theme> readThemes() {
        String sql = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Theme createTheme(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO theme (name, description, thumbnail) values (?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, theme.getName());
            preparedStatement.setString(2, theme.getDescription());
            preparedStatement.setString(3, theme.getThumbnail());
            return preparedStatement;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return theme.changeId(id);
    }

    public void deleteTheme(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
