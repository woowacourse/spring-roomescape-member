package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Theme> themeRowMapper;

    public ThemeDao(
            final DataSource dataSource,
            final RowMapper<Theme> themeRowMapper
    ) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("ID");
        this.themeRowMapper = themeRowMapper;
    }

    public Theme save(final Theme theme) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("NAME", theme.getName())
                .addValue("DESCRIPTION", theme.getDescription())
                .addValue("THUMBNAIL", theme.getThumbnail());

        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return theme.assignId(id);
    }

    public List<Theme> getAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Optional<Theme> findById(final long themeId) {
        String sql = "SELECT * FROM theme WHERE id = ? ";
        return jdbcTemplate.query(sql, themeRowMapper, themeId)
                .stream()
                .findAny();
    }

    public void delete(final long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, Long.valueOf(id));
    }
}
