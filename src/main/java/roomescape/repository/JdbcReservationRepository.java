package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<ReservationEntity> ROW_MAPPER = (rs, rowNum) -> {
        ReservationTime time = new ReservationTime(
                rs.getTime("time_start_at").toLocalTime()
        );
        Theme theme = new Theme(
                rs.getString("theme_name"),
                rs.getString("theme_description"),
                rs.getString("theme_thumbnail")
        );
        Reservation reservation = new Reservation(
                rs.getString("reservation_name"),
                rs.getDate("reservation_date").toLocalDate(),
                time,
                theme
        );
        return new ReservationEntity(
                rs.getLong("reservation_id"),
                rs.getLong("time_id"),
                rs.getLong("theme_id"),
                reservation
        );
    };

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationEntity> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name AS reservation_name,
                    r.date AS reservation_date,
                    t.id AS time_id,
                    t.start_at AS time_start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail_url AS theme_thumbnail
                FROM reservation r
                INNER JOIN reservation_time t ON r.time_id = t.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public ReservationEntity save(Reservation reservation, Long timeId, Long themeId) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, timeId);
            ps.setLong(4, themeId);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationEntity(id, timeId, themeId, reservation);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
