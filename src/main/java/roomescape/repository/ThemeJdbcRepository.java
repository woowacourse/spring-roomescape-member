package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.exception.exceptions.ExistingEntryException;
import roomescape.exception.exceptions.ReferencedRowExistsException;

@Repository
public class ThemeJdbcRepository implements ThemeRepository {
    private static final RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;


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
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, themeRowMapper, themeId);
    }

    public List<Theme> findHotThemesByDurationAndCount(LocalDate start, LocalDate end, Integer page, Integer size) {
        String sql = """
                SELECT
                    th.id AS id,
                    th.name AS name,
                    th.description AS description,
                    th.thumbnail AS thumbnail
                FROM
                    reservation AS r
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY id
                ORDER BY COUNT(r.id) DESC, name
                LIMIT ?
                OFFSET ?;
                """;
        return jdbcTemplate.query(sql, themeRowMapper, start, end, page, size);
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
            throw new ExistingEntryException("이미 동일한 이름을 가진 테마가 존재합니다.");
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new ReferencedRowExistsException("현 테마에 예약이 존재합니다.");
        }
    }
}
