package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeJdbcRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("ID");
    }

    @Override
    public List<Theme> findAll() {
        final String sql = "SELECT * FROM THEME";

        return jdbcTemplate.query(sql, this::mapToTheme);
    }

    @Override
    public Long save(final Theme theme) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("ID", theme.id())
                .addValue("NAME", theme.id())
                .addValue("DESCRIPTION", theme.description())
                .addValue("THUMBNAIL", theme.thumbnail());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        final String sql = "SELECT * FROM THEME WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapToTheme, id)
                .stream()
                .findAny();
    }

    @Override
    public Boolean removeById(final Long id) {
        final String sql = "DELETE FROM THEME WHERE ID = ?";

        int removedRowsCount = jdbcTemplate.update(sql, id);
        return removedRowsCount > 0;
    }

    private Theme mapToTheme(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getString("THUMBNAIL")
        );
    }
}
