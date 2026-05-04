package roomescape.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT r.id,r.name,r.date,rt.id AS time_id, rt.start_at,
                        t.id AS theme_id, t.name AS theme_name, t.description, t.url
                        FROM reservation r
                        INNER JOIN reservation_time rt ON r.time_id = rt.id
                        INNER JOIN theme t ON r.theme_id = t.id;
                    """,
                (rs, rowNum) -> {
                    ReservationTime time = new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    );
                    Theme theme = new Theme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("url")
                    );
                    return new Reservation(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getDate("date").toLocalDate(),
                            time,
                            theme
                    );
                }
        );
    }

    public boolean existsBy(Theme theme, ReservationTime time) {
        Boolean result = jdbcTemplate.queryForObject("""
        SELECT EXISTS(
            SELECT *
            FROM reservation
            WHERE time_id = ?
              AND theme_id = ?
        )
        """,
                Boolean.class,
                time.getId(),
                theme.getId()
        );
        return Boolean.TRUE.equals(result);
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", reservation.getName());
        params.put("date", reservation.getDate());
        params.put("time_id", reservation.getTime().getId());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

}
