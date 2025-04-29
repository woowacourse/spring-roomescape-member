package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeDao {

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> {
        return new Theme(
               resultSet.getLong("id"),
               resultSet.getString("name"),
               resultSet.getString("description"),
               resultSet.getString("thumbnail"));
    };

    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }
}
