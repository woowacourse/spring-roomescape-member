package roomescape.theme.dao;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.exception.RoomEscapeException;
import roomescape.theme.domain.Theme;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("THEME")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<Theme> rowMapper = ((resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    ));

    public List<Theme> findAll() {
        final String sql = "select * from theme order by id asc";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Theme getById(final long id) {
        final String sql = "select * from theme where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (final EmptyResultDataAccessException exception) {
            throw new RoomEscapeException("해당 themeId와 일치하는 테마가 존재하지 않습니다.");
        }
    }

    public Theme save(final Theme theme) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        final Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Theme(id, theme);
    }

    public int deleteById(final long id) {
        final String sql = "delete from theme where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Theme> findPopularThemes(final LocalDate startDate, final LocalDate lastDate) {
        final String sql = """
                SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count 
                FROM theme t 
                JOIN reservation r ON t.id = r.theme_id 
                WHERE r.date BETWEEN ? AND ? 
                GROUP BY t.id, t.name, t.description, t.thumbnail 
                ORDER BY reservation_count DESC 
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql, rowMapper, startDate, lastDate);
    }

    public boolean checkExistThemes(final Theme theme) {
        String sql = "SELECT EXISTS (SELECT 1 FROM theme WHERE name = ? AND description = ? AND thumbnail = ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
        return Boolean.TRUE.equals(result);
    }
}
