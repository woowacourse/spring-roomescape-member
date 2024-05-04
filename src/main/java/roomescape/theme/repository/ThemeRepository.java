package roomescape.theme.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ThemeRepository {
    private static final RowMapper<Theme> rowMapper = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        final String sql = "select * from theme";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public long save(final Theme theme) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public int deleteById(final long id) {
        final String sql = "delete from theme where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Theme> findPopular(final String startDate, final String lastDate) {
        final String sql = "SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count " +
                "FROM theme t " +
                "JOIN reservation r ON t.id = r.theme_id " +
                "WHERE r.date BETWEEN ? AND ? " +
                "GROUP BY t.id, t.name, t.description, t.thumbnail " +
                "ORDER BY reservation_count DESC " +
                "LIMIT 10;";
        return jdbcTemplate.query(sql, rowMapper, startDate, lastDate);
    }
}
