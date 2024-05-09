package roomescape.web.repository;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.Theme;
import roomescape.core.repository.ThemeRepository;
import roomescape.web.exception.NotFoundException;

@Repository
public class ThemeRepositoryImpl implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Theme> findAll() {
        final String query = "SELECT id, name, description, thumbnail FROM theme";
        return jdbcTemplate.query(query, getThemeRowMapper());
    }

    @Override
    public List<Theme> findPopularThemesByPeriod(final long periodDay) {
        final LocalDate endDate = LocalDate.now();
        final LocalDate startDate = endDate.minusDays(periodDay);

        final String query = """
                SELECT t.id, t.name, t.description, t.thumbnail
                FROM theme AS t
                JOIN reservation AS r
                ON t.id = r.theme_id
                WHERE r.date BETWEEN ? AND ?
                GROUP BY t.id
                ORDER BY count(r.id) DESC
                LIMIT 10
                """;
        return jdbcTemplate.query(query, getThemeRowMapper(), startDate, endDate);
    }

    @Override
    public Theme findById(final long id) {
        try {
            final String query = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
            return jdbcTemplate.queryForObject(query, getThemeRowMapper(), id);
        } catch (DataAccessException e) {
            throw new NotFoundException("테마를 찾을 수 없습니다.");
        }
    }

    @Override
    public boolean hasDuplicateTheme(final String name) {
        final String query = "SELECT EXISTS(SELECT 1 FROM theme WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, name));
    }

    @Override
    public void deleteById(final long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
