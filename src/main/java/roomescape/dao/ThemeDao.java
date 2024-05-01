package roomescape.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.mapper.ThemeRowMapper;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ThemeRowMapper rowMapper;

    public ThemeDao(JdbcTemplate jdbcTemplate, DataSource dataSource, ThemeRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public Theme create(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnailAsString());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getThumbnailAsString());
    }

    public List<Theme> getAll() {
        String sql = "SELECT id, name,description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

}
