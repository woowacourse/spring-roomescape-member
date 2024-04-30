package roomescape.theme.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Theme> rowMapper;

    public ThemeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final RowMapper<Theme> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    public List<Theme> findAll() {
        final String sql = "select * from theme";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
