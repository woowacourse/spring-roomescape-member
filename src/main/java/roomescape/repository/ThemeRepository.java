package roomescape.repository;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Theme;

@Repository
public class ThemeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ThemeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("select * from theme", (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        ));
    }

    public Theme save(Theme theme) {
        long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                "name", theme.getName(), "description", theme.getDescription(), "thumbnail", theme.getThumbnail()))
                .longValue();

        return new Theme(
                reservationTimeId,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail());
    }
}
