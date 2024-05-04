package roomescape.repository.theme;

import java.time.LocalDate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import roomescape.exceptions.UserException;

@Repository
public class ThemeH2Repository implements ThemeRepository {

    private static final String TABLE_NAME = "THEME";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeH2Repository(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName().getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean hasTheme(Name name) {
        String sql = "SELECT * FROM theme WHERE name = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, name.getName()).isEmpty();
    }

    @Override
    public void delete(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new UserException("참조되고 있는 테마를 삭제할 수 없습니다. id = " + id);
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        try {
            Theme theme = jdbcTemplate.queryForObject(
                    "SELECT * FROM theme WHERE id = ?",
                    getThemeRowMapper(),
                    id
            );
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Theme> getThemeRowMapper() {
        return (resultSet, rowNum) -> new Theme(
                resultSet.getLong("id"),
                new Name(resultSet.getString("name")),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM theme",
                getThemeRowMapper()
        );
    }

    @Override
    public List<Theme> findTrendings(LocalDate start, LocalDate end, Long limit) {
        String sql = "SELECT t.id, t.name, t.description, t.thumbnail, COUNT(r.id) AS reservation_count " +
                "FROM theme t " +
                "LEFT JOIN reservation r ON t.id = r.theme_id AND r.date >= ? AND r.date <= ? " +
                "GROUP BY t.id " +
                "ORDER BY reservation_count DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, getThemeRowMapper(), start, end, limit);
    }
}
