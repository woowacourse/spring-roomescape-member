package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ThemeRepository implements ThemeRepository {

    private final String TABLE_NAME = "THEME";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public H2ThemeRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("ID");
    }

    private Theme mapRowTheme(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getString("THUMBNAIL")
        );
    }

    private Theme mapRowThemeWithTableName(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(
                rs.getLong(TABLE_NAME + ".ID"),
                rs.getString(TABLE_NAME + ".NAME"),
                rs.getString(TABLE_NAME + ".DESCRIPTION"),
                rs.getString(TABLE_NAME + ".THUMBNAIL")
        );
    }

    @Override
    public List<Theme> findAll() {
        String sql = "SELECT * FROM THEME";

        return jdbcTemplate.query(sql, this::mapRowTheme);
    }

    @Override
    public Optional<Theme> findById(long id) {
        String sql = "SELECT * FROM THEME WHERE ID = ?";

        return jdbcTemplate.query(sql, this::mapRowTheme, id)
                .stream()
                .findAny();
    }

    @Override
    public List<Theme> findPopularThemes(
            LocalDate from,
            LocalDate to,
            int limit
    ) {
        String sql = """
                SELECT T.ID, T.NAME, T.THUMBNAIL, T.DESCRIPTION, COUNT(T.ID) AS FREQUENCY
                FROM THEME AS T
                INNER JOIN RESERVATION R ON R.THEME_ID = T.ID
                WHERE R.DATE BETWEEN ? AND ?
                GROUP BY (T.ID)
                ORDER BY FREQUENCY DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, this::mapRowThemeWithTableName, from, to, limit);
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(theme);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return theme.assignId(id);
    }

    @Override
    public int delete(long id) {
        String sql = "DELETE FROM THEME WHERE ID = ?";

        return jdbcTemplate.update(sql, id);
    }
}
