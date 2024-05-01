package roomescape.domain.theme.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.global.query.QueryBuilder;

@Repository
public class JdbcThemeRepository implements ThemeRepository {

    private static final RowMapper<Theme> ROW_MAPPER = (rs, rowNum) -> new Theme(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("thumbnail")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations simpleJdbcInsert;

    public JdbcThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Theme> findAll() {
        String query = QueryBuilder.select("theme")
                .addAllColumns()
                .build();

        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public void deleteById(long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public boolean existsByName(String name) {
        String query = "SELECT id FROM theme WHERE EXISTS (SELECT 1 FROM theme WHERE name = ?)";

        try {
            jdbcTemplate.queryForObject(query, Long.class, name);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public Optional<Theme> findById(long id) {
        String query = "SELECT * FROM theme WHERE id = ?";
        try {
            Theme theme = jdbcTemplate.queryForObject(query, ROW_MAPPER, id);
            return Optional.ofNullable(theme);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
