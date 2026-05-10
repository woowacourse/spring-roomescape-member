package roomescape.repository.dao;

import java.time.LocalDate;
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

    private static final RowMapper<Theme> themeRowMapper = (rs, rowNum) ->
            new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("image_url")
            );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> selectAll() {
        String sql = "select * from theme where is_deleted = ?;";
        return jdbcTemplate.query(sql, themeRowMapper, false);
    }

    public Theme insert(Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("image_url", theme.getImageUrl())
                .addValue("is_deleted", false);

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Theme(id, theme.getName(), theme.getDescription(), theme.getImageUrl());
    }

    public int deleteById(Long id) {
        String sql = "update theme set is_deleted = true where id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public Optional<Theme> selectById(Long id) {
        String sql = "select * from theme where id = ? AND is_deleted = FALSE;";
        return jdbcTemplate.query(sql, themeRowMapper, id)
                .stream()
                .findFirst();
    }

    public Theme getById(Long id) {
        String sql = "select * from theme where id = ? AND is_deleted = FALSE;";
        return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
    }

    public List<Theme> findThemesOrderByReservationCountDesc(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                SELECT
                    t.id,
                    t.name,
                    t.description,
                    t.image_url,
                    t.is_deleted
                FROM theme t
                INNER JOIN reservation r ON t.id = r.theme_id
                WHERE t.is_deleted = FALSE
                  AND r.date >= ?
                  AND r.date <= ?
                GROUP BY t.id, t.name, t.description, t.image_url
                ORDER BY COUNT(r.id) DESC , t.name
                LIMIT ?
                """;

        return jdbcTemplate.query(sql, themeRowMapper, startDate, endDate, limit);
    }
}
