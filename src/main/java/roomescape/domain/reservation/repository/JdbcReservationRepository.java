package roomescape.domain.reservation.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAllReservations() {

        return jdbcTemplate.query(
            """
                SELECT r.id, r.name, r.date, rt.id AS time_id, rt.start_at, t.id AS theme_id, t.name AS theme_name, t.description, t.image_url
                FROM reservation r
                JOIN reservation_time rt ON r.time_id = rt.id
                JOIN theme t ON r.theme_id = t.id
                """,
            (rs, rowNum) -> mapReservation(rs)
        );
    }

    @Override
    public Reservation save(Reservation reservation) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().getId());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();

        return Reservation.reconstruct(key.longValue(), reservation.getName(), reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme());
    }

    private Reservation mapReservation(java.sql.ResultSet rs) throws java.sql.SQLException {
        return Reservation.reconstruct(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            Time.reconstruct(
                rs.getLong("time_id"),
                LocalTime.parse(rs.getString("start_at"))
            ),
            Theme.reconstruct(
                rs.getLong("theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("image_url")
            )
        );
    }

    @Override
    public void deleteReservationById(Long id) {
        jdbcTemplate.update(
            "DELETE FROM reservation WHERE id = ?",
            id
        );
    }
}
