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
    private static final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> readAll() {
        String sql = """
                SELECT * FROM theme""";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Theme read(long themeId) {
        String sql = """
                SELECT * FROM theme WHERE id = ?""";

        return jdbcTemplate.queryForObject(sql, ROW_MAPPER, themeId);
    }

    public Long create(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public Integer delete(long id) {
        String sql = """
                DELETE FROM theme WHERE id = ?""";

        return jdbcTemplate.update(sql, id);
    }

    public List<Theme> findPopular(String startDate, String lastDate) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count\s
                FROM theme t\s
                JOIN reservation r ON t.id = r.theme_id\s
                WHERE r.date BETWEEN ? AND ?\s
                GROUP BY t.id, t.name, t.description, t.thumbnail\s
                ORDER BY reservation_count DESC\s
                LIMIT 10""";
        
        return jdbcTemplate.query(sql, ROW_MAPPER, startDate, lastDate);
    }
}
