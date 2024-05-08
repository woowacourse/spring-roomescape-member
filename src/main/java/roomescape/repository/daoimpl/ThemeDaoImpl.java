package roomescape.repository.daoimpl;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.repository.ThemeDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
class ThemeDaoImpl implements ThemeDao {
    private static final String TABLE_NAME = "theme";
    private static final String KEY_COLUMN_NAME = "id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Theme> themeRowMapper;

    public ThemeDaoImpl(
            final JdbcTemplate jdbcTemplate,
            final DataSource source,
            final RowMapper<Theme> themeRowMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
        this.themeRowMapper = themeRowMapper;
    }

    @Override
    public Theme save(final Theme theme) {
        try {
            SqlParameterSource params = makeInsertParams(theme);
            long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            return makeSavedTheme(theme, id);
        } catch (DuplicateKeyException e) {
            throw new IllegalStateException("[ERROR] 키 값 에러 : 중복된 테마 키가 존재합니다.");
        }
    }

    @Override
    public List<Theme> getAll() {
        String sql = "SELECT * FROM theme";
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    @Override
    public Optional<Theme> findById(final long themeId) {
        String sql = "SELECT * FROM theme WHERE id = ? ";
        return jdbcTemplate.query(sql, themeRowMapper, themeId)
                .stream()
                .findAny();
    }

    @Override
    public void delete(final long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(sql, Long.valueOf(id));
    }

    private Theme makeSavedTheme(Theme theme, long id) {
        return new Theme(
                id,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    private MapSqlParameterSource makeInsertParams(Theme theme) {
        return new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
    }
}
