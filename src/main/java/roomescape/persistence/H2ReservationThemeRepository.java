package roomescape.persistence;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.business.ReservationTheme;

@Repository
public class H2ReservationThemeRepository implements ReservationThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2ReservationThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
