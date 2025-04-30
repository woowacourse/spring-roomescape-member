package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;

@Repository
public class H2ReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public H2ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String query = """
                SELECT
                    r.id as reservation_id, r.name, r.date,
                    t.id as time_id, t.start_at,
                    th.id as theme_id, th.name, th.description, th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t
                INNER JOIN theme as th
                ON r.time_id = t.id AND r.theme_id = th.id
                """;
        return jdbcTemplate.query(query, (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("name"),
                        rs.getObject("date", LocalDate.class),
                        new ReservationTime(
                                rs.getLong("time_id"),
                                rs.getObject("start_at", LocalTime.class)
                        ),
                        new ReservationTheme(
                                rs.getLong("theme_id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("thumbnail")
                        )
                )
        );
    }

    @Override
    public Reservation findById(Long id) {
        String query = """
                SELECT
                    r.id as reservation_id, r.name, r.date,
                    t.id as time_id, t.start_at,
                    th.id as theme_id, th.name, th.description, th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t
                INNER JOIN theme as th
                ON r.time_id = t.id AND r.theme_id = th.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new Reservation(
                        rs.getLong("reservation_id"),
                        rs.getString("name"),
                        rs.getObject("date", LocalDate.class),
                        new ReservationTime(
                                rs.getLong("time_id"),
                                rs.getObject("start_at", LocalTime.class)
                        ),
                        new ReservationTheme(
                                rs.getLong("theme_id"),
                                rs.getString("name"),
                                rs.getString("description"),
                                rs.getString("thumbnail")
                        )
                ), id);
    }

    @Override
    public Long add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        Long id = (Long) jdbcInsert.executeAndReturnKey(parameters);
        reservation.setId(id);
        return id;
    }

    @Override
    public void deleteById(Long id) {
        String query = "delete from RESERVATION where id=?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public boolean existsByDateTime(LocalDate date, LocalTime time) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    JOIN reservation_time t
                        ON r.time_id = t.id
                    WHERE r.date = ? AND t.start_at = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, date, time));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation r
                    WHERE r.time_id = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    @Override
    public boolean existByThemeId(Long themeId) {
        String query = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation
                    WHERE theme_id = ?
                )
                """;
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }
}
