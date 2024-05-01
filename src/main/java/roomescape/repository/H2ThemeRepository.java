package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2ThemeRepository implements ThemeRepository {
    private final ThemeRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2ThemeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new ThemeRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("select * from theme", rowMapper);
    }

    public Optional<Theme> findById(long id) {
        String sql = "select * from theme where id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    public Theme save(Theme theme) {
        long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", theme.getName(),
                        "description", theme.getDescription(),
                        "thumbnail", theme.getThumbnail()))
                .longValue();

        return new Theme(
                reservationTimeId,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }

    public void deleteById(long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    static class ThemeRowMapper implements RowMapper<Theme> {
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail"));
        }
    }
}
