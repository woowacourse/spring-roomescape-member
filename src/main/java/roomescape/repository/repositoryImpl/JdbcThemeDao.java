package roomescape.repository.repositoryImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

@Repository
public class JdbcThemeDao implements ThemeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Theme> timeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );
    private RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    public JdbcThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        List<Theme> themes = jdbcTemplate.query("SELECT * FROM theme", timeRowMapper);
        return Collections.unmodifiableList(themes);
    }

    @Override
    public Theme findById(long id) {
        String sql = "SELECT * FROM theme WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, themeRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("[ERROR] 존재하지 않는 테마입니다.");
        }
    }

    @Override
    public List<Theme> findThemesByDescOrder() {
        String nowDate = LocalDate.now().toString();
        String weekBeforeDate = LocalDate.now().minusDays(7).toString();
        return jdbcTemplate.query("""
                SELECT
                th.id as theme_id,
                th.name,
                th.description,
                th.thumbnail,
                COUNT(r.theme_id) AS reservation_count
                FROM theme AS th
                INNER JOIN reservation AS r ON r.theme_id = th.id
                WHERE r.date < ? AND r.date > ?
                GROUP BY th.id, th.name, th.description, th.thumbnail
                ORDER BY reservation_count DESC
                LIMIT 10
                """, themeRowMapper, nowDate, weekBeforeDate);
    }

    @Override
    public long save(Theme theme) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(theme);
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        return newId.longValue();
    }

    @Override
    public boolean existByName(final String name) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme WHERE name = ?", Integer.class, name);
        return count > 0;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }
}
