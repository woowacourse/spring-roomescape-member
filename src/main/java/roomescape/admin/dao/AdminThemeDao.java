package roomescape.admin.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class AdminThemeDao {

    private static final RowMapper<Theme> rowMapper =
            (rs, rowNum) -> {
                return new Theme(
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("image"));
            };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public AdminThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> selectAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Theme selectById(Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Theme insert(Theme theme) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image", theme.getImage());
        long id = (long) simpleJdbcInsert.executeAndReturnKey(params);
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getImage());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM theme where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
