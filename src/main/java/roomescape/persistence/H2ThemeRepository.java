package roomescape.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;
import roomescape.domain.repository.ThemeRepository;

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

    public Theme save(Theme theme) {
        Long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
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

    public List<Theme> findAll() {
        return jdbcTemplate.query("select * from theme", rowMapper);
    }

    public Optional<Theme> findById(Long id) {
        String sql = "select * from theme where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        try {
            String sql = "delete from theme where id = ?";
            jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("해당 테마를 참조하는 예약이 존재합니다.");
        }
    }

    private static class ThemeRowMapper implements RowMapper<Theme> {
        public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail"));
        }
    }
}
