package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class H2ThemeRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2ThemeRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    private Theme mapRowTheme(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        );
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, this::mapRowTheme);
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";

        return jdbcTemplate.query(sql, this::mapRowTheme, id)
                .stream()
                .findAny();
    }

    @Override
    public Theme save(final Theme theme) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(theme);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return theme.assignId(id);
    }

    @Override
    public int delete(final Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }
}
