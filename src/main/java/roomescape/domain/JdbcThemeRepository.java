package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail FROM theme", getThemeRowMapper());
    }

    public Theme create(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public void removeById(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalStateException("해당 테마를 사용하고 있는 예약이 존재합니다.");
        }
    }

    public Optional<Theme> findById(Long id) {
        try {
            Theme theme = jdbcTemplate.queryForObject(
                    "SELECT id, name, description, thumbnail FROM theme WHERE id = ?",
                    getThemeRowMapper(), id);
            return Optional.of(theme);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    public List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, int limitCount) {
        return jdbcTemplate.query("""
                        SELECT t.id, t.name, t.description, t.thumbnail
                        FROM theme as t
                        LEFT JOIN reservation AS r
                        ON t.id = r.theme_id
                        AND convert(r.date, DATE) BETWEEN ? AND ?
                        GROUP BY t.id
                        ORDER BY count(r.id) DESC, t.id ASC
                        LIMIT ?""",
                getThemeRowMapper(), startDate, endDate, limitCount);
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));
    }
}
