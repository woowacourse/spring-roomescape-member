package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class H2ThemeRepository implements ThemeRepository {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String THUMBNAIL = "thumbnail";

    private final ThemeRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2ThemeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new ThemeRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns(ID);
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("select * from theme", rowMapper);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = ?";
        try {
            Theme savedTheme = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(savedTheme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Theme save(Theme theme) {
        Long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                        NAME, theme.getName(),
                        DESCRIPTION, theme.getDescription(),
                        THUMBNAIL, theme.getThumbnail()
        )).longValue();

        return new Theme(
                reservationTimeId,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public void deleteById(Long id) {
        String sql = "delete from theme where id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class ThemeRowMapper implements RowMapper<Theme> {
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Theme(
                    rs.getLong(ID),
                    rs.getString(NAME),
                    rs.getString(DESCRIPTION),
                    rs.getString(THUMBNAIL)
            );
        }
    }
}
