package roomescape.theme.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Long save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<Theme> findById(long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";

        try {
            Theme theme = jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Integer deleteById(long id) {
        String sql = "DELETE FROM theme WHERE id = ?";

        return jdbcTemplate.update(sql, id);
    }

    public List<Theme> findPopular(LocalDate startDate, LocalDate lastDate) {
        String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count
                FROM theme t
                JOIN reservation r ON t.id = r.theme_id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id, t.name, t.description, t.thumbnail
                ORDER BY reservation_count DESC
                LIMIT 10""";

        return jdbcTemplate.query(sql, ROW_MAPPER, startDate, lastDate);
    }
}
