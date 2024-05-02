package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ThemeDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeDAO(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public Theme insert(final Theme theme) {
        String name = theme.getName();
        String description = theme.getDescription();
        String thumbnail = theme.getThumbnail();

        final SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail);

        final long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Theme(id, name, description, thumbnail);
    }

    public Theme findById(final Long id) {
        final String sql = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, themeRowMapper(), id);
    }

    public List<Theme> selectAll() {
        final String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Theme> findTopRanking(LocalDate firstDayOfPeriod, LocalDate lastDayOfPeriod) {
        String sql =
                "SELECT t.id, t.name, t.description, t.thumbnail, IFNULL(COUNT(r.id), 0) AS reservation_count " +
                        "FROM theme AS t " +
                        "LEFT JOIN reservation AS r ON t.id = r.theme_id AND r.date BETWEEN ? AND ? " +
                        "GROUP BY t.id, t.name, t.description, t.thumbnail " +
                        "ORDER BY reservation_count DESC, t.id LIMIT 10";
        return jdbcTemplate.query(sql, themeRowMapper(), firstDayOfPeriod, lastDayOfPeriod);
    }

    private RowMapper<Theme> themeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
