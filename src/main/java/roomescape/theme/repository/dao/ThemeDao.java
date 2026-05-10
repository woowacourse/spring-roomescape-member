package roomescape.theme.repository.dao;

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
import roomescape.theme.repository.entity.ThemeEntity;

@Repository
public class ThemeDao {

    private static final RowMapper<ThemeEntity> themeEntityRowMapper = (rs, rowNum) ->
            new ThemeEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("image_url"),
                    rs.getBoolean("is_deleted")
            );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<ThemeEntity> selectAll() {
        String sql = "select * from theme where is_deleted = ?;";
        return jdbcTemplate.query(sql, themeEntityRowMapper, false);
    }

    public Long insert(String name, String description, String imageUrl) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("image_url", imageUrl)
                .addValue("is_deleted", false);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    public int deleteById(Long id) {
        String sql = "update theme set is_deleted = true where id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public Optional<ThemeEntity> selectById(Long id) {
        String sql = "select * from theme where id = ? AND is_deleted = FALSE;";
        return jdbcTemplate.query(sql, themeEntityRowMapper, id)
                .stream()
                .findFirst();
    }

    public ThemeEntity getById(Long id) {
        String sql = "select * from theme where id = ? AND is_deleted = FALSE;";
        return jdbcTemplate.queryForObject(sql, themeEntityRowMapper, id);
    }

    public List<ThemeEntity> findThemesOrderByReservationCountDesc(LocalDate startDate, LocalDate endDate, int limit) {
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

        return jdbcTemplate.query(sql, themeEntityRowMapper, startDate, endDate, limit);
    }
}
