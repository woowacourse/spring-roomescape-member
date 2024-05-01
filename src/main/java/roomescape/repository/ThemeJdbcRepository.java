package roomescape.repository;

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

@Repository
public class ThemeJdbcRepository implements ThemeRepository {

    private static final String TABLE_NAME = "theme";

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
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description, thumbnail FROM " + TABLE_NAME;
        return jdbcTemplate.query(sql, themeRowMapper);
    }

    public Theme findByThemeId(Long themeId) {
        String sql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, themeRowMapper, themeId);
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
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("현 테마에 예약이 존재합니다.");
        }
    }
}
