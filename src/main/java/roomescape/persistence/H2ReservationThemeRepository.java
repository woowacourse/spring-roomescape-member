package roomescape.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.ReservationTheme;

@Repository
public class H2ReservationThemeRepository implements ReservationThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public H2ReservationThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTheme> findAll() {
        String query = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new ReservationTheme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }

    @Override
    public Long add(ReservationTheme reservationTheme) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservationTheme.getName());
        parameters.put("description", reservationTheme.getDescription());
        parameters.put("thumbnail", reservationTheme.getThumbnail());
        return (long) jdbcInsert.executeAndReturnKey(parameters);
    }

    @Override
    public boolean existByName(String name) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM theme
                    WHERE name = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, name));
    }
}
