package roomescape.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class JdbcThemeDAO implements ThemeDAO {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNumber) ->
            new Theme(resultSet.getLong("id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcThemeDAO(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(final Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("theme_name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean existsByName(String name) {
        String query = "SELECT EXISTS (SELECT 1 FROM theme WHERE theme_name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, name));
    }

    @Override
    public List<Theme> findAll() {
        String query = "SELECT * FROM theme";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER);
    }

    @Override
    public Optional<Theme> findById(final long id) {
        String query = "SELECT * FROM theme WHERE id = ?";
        return jdbcTemplate.query(query, THEME_ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean deleteById(long id) {
        String query = "DELETE FROM theme WHERE id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }
}
