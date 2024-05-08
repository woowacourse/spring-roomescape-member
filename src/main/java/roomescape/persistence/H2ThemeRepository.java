package roomescape.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
        if (theme.getThumbnail().isEmpty()) {
            return saveWithoutThumbnail(theme);
        }
        return saveWithAllField(theme);
    }

    private Theme saveWithAllField(Theme theme) {
        Long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", theme.getName(),
                        "description", theme.getDescription(),
                        "thumbnail", theme.getThumbnail().get()))
                .longValue();

        return new Theme(
                reservationTimeId,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail().get());
    }

    private Theme saveWithoutThumbnail(Theme theme) {
        Long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                        "name", theme.getName(),
                        "description", theme.getDescription()))
                .longValue();

        return new Theme(
                reservationTimeId,
                theme.getName(),
                theme.getDescription());
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
            throw new IllegalStateException("해당 테마를 참조하는 예약이 존재합니다.");
        }
    }

    public List<Theme> findPastReservations(LocalDate startDate, LocalDate endDate, int limitCount) {
        String sql = """
                    select t.id, t.name, t.description, t.thumbnail
                    from theme as t
                    left join reservation as r
                    on t.id = r.theme_id
                    and convert(r.date, date) between ? and ?
                    group by t.id
                    order by count(r.id) desc, t.id asc
                    limit ?
                """;

        return jdbcTemplate.query(sql, rowMapper, startDate, endDate, limitCount);
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
