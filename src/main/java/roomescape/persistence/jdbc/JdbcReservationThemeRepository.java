package roomescape.persistence.jdbc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.entity.ReservationThemeEntity;

@Repository
public class JdbcReservationThemeRepository implements ReservationThemeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcReservationThemeRepository(JdbcTemplate jdbcTemplate) {
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
        List<ReservationThemeEntity> reservationThemeEntities = jdbcTemplate.query(
                query,
                (rs, rowNum) -> new ReservationThemeEntity(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
        return reservationThemeEntities.stream()
                .map(ReservationThemeEntity::toDomain)
                .toList();
    }

    @Override
    public ReservationTheme add(ReservationTheme reservationTheme) {
        ReservationThemeEntity reservationThemeEntity = ReservationThemeEntity.fromDomain(reservationTheme);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservationThemeEntity.getName());
        parameters.put("description", reservationThemeEntity.getDescription());
        parameters.put("thumbnail", reservationThemeEntity.getThumbnail());
        long id = jdbcInsert.executeAndReturnKey(parameters).longValue();
        return reservationThemeEntity.copyWithId(id).toDomain();
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
        return jdbcTemplate.query(
                        query,
                        (rs, rowNum) -> new ReservationThemeEntity(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("thumbnail")
                        ),
                        id
                )
                .stream()
                .findFirst()
                .map(ReservationThemeEntity::toDomain);
    }

    @Override
    public List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start,
                                                                               LocalDate end,
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
        List<ReservationThemeEntity> themeEntities = jdbcTemplate.query(
                query,
                (rs, rowNum) -> new ReservationThemeEntity(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                ),
                start,
                end,
                limit
        );
        return themeEntities.stream()
                .map(ReservationThemeEntity::toDomain)
                .toList();
    }
}
