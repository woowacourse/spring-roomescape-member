package roomescape.repository.theme;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class ThemeH2Repository implements ThemeRepository{

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
        if (isDuplicatedTheme(theme)) {
            throw new IllegalArgumentException("이미 존재하는 테마입니다.");
        }

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName().getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    private boolean isDuplicatedTheme(Theme theme) {
        String sql = "SELECT * FROM theme WHERE name = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, theme.getName().getName()).isEmpty();
    }

    @Override
    public void delete(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("참조되고 있는 테마를 삭제할 수 없습니다. id = " + id);
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
}
