package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ThemeRepository {

    private static final RowMapper<Theme> ROW_MAPPER = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "SELECT * FROM theme";

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Theme save(Theme theme) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", theme.getName())
                .addValue("description", theme.getDescription())
                .addValue("thumbnail", theme.getThumbnail());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Theme(id, theme);
    }

    public int delete(Long id) {
        String sql = "DELETE FROM theme WHERE id = ?";
        int deleteCount = jdbcTemplate.update(sql, id);

        return deleteCount;
    }
}
