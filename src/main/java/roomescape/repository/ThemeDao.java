package roomescape.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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
        final SqlParameterSource params = new BeanPropertySqlParameterSource(theme);
        final long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return theme.assignId(id);
    }

    public List<Theme> getAll() {
        return jdbcTemplate.query("SELECT * FROM theme", themeRowMapper);
    }

    public Optional<Theme> findById(final long id) {
        try {
            String sql = "SELECT * FROM theme WHERE id = ? ";
            return Optional.of(jdbcTemplate.queryForObject(sql, themeRowMapper, id));
        } catch (final EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(final long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", Long.valueOf(id));
    }
}
