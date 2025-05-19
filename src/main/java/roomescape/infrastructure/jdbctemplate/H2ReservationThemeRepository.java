package roomescape.infrastructure.jdbctemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;

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

    private final RowMapper<ReservationTheme> reservationThemeRowMapper = (rs, rowNum) -> (
            new ReservationTheme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("thumbnail")
            )
    );

    @Override
    public List<ReservationTheme> findAll() {
        String query = """
                SELECT id, name, description, thumbnail
                FROM theme
                """;
        return jdbcTemplate.query(query, reservationThemeRowMapper);
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
    public void deleteById(Long id) {
        String query = """
                DELETE FROM theme
                WHERE id = ?
                """;
        jdbcTemplate.update(query, id);
    }

    @Override
    public Optional<ReservationTheme> findById(Long id) {
        String query = """
                SELECT id, name, description, thumbnail
                FROM theme
                WHERE id = ?
                """;
        return jdbcTemplate.query(query, reservationThemeRowMapper,
                        id
                )
                .stream()
                .findFirst();
    }

    @Override
    public List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start, LocalDate end,
                                                                               int limit) {
        String query = """
                SELECT th.id, th.name, th.description, th.thumbnail
                FROM theme th
                JOIN reservation r
                    ON th.id = r.theme_id
                WHERE r.date >= ? AND r.date <= ?
                GROUP BY th.id
                ORDER BY COUNT(th.id) DESC
                LIMIT ?
                """;
        return jdbcTemplate.query(query, reservationThemeRowMapper,
                start,
                end,
                limit
        );
    }
}
