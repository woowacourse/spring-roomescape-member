package roomescape.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public ThemeJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Theme findByThemeId(Long themeId) {
        try {
            String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, themeRowMapper, themeId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }

    public List<Theme> findWeeklyHotThemes() {
        String sql = """
                SELECT
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail,
                    COUNT(r.id) AS reservation_count
                FROM
                    reservation AS r
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.date BETWEEN DATEADD(DAY, -7, CURDATE()) AND CURDATE()
                GROUP BY theme_id
                ORDER BY reservation_count DESC, theme_name
                LIMIT 10;
                """;
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Theme save(Theme theme) {
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("name", theme.getName())
                    .addValue("description", theme.getDescription())
                    .addValue("thumbnail", theme.getThumbnail());
            Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
            return new Theme(
                    id,
                    theme.getName(),
                    theme.getDescription(),
                    theme.getThumbnail()
            );
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 동일한 이름을 가진 테마가 존재합니다.");
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("현 테마에 예약이 존재합니다.");
        }
    }
}
