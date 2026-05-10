package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getObject("date", LocalDate.class),
            new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getObject("start_at", LocalTime.class)
            ),
            new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("description"),
                    rs.getString("url")
            )
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String selectSql =
                "SELECT r.id, r.username, r.date, t.id as time_id, t.start_at, m.id as theme_id, m.name as theme_name, m.description, m.url  "
                        +
                        "FROM reservation r " +
                        "INNER JOIN reservation_time t ON r.time_id = t.id " +
                        "INNER JOIN theme m ON r.theme_id = m.id ";
        return jdbcTemplate.query(selectSql, reservationMapper);
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation(username, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setObject(2, reservation.getDate());
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(),
                reservation.getTime(), reservation.getTheme());
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId) > 0;
    }

    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, timeId) > 0;
    }

    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE theme_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, themeId) > 0;
    }
}
