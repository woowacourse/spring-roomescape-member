package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ThemeResponse;
import roomescape.dto.TimeResponse;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    public Reservation save(ReservationRequest reservationRequest, TimeResponse timeResponse,
                            ThemeResponse themeResponse) {
        String sql = "INSERT INTO reservation(username, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationRequest.name());
            ps.setObject(2, reservationRequest.date());
            ps.setLong(3, reservationRequest.timeId());
            ps.setLong(4, reservationRequest.themeId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Reservation(id, reservationRequest.name(), reservationRequest.date(),
                ReservationTime.from(timeResponse), Theme.from(themeResponse));
    }

    public Reservation findById(Long id) {
        String sql =
                "SELECT r.id, r.username, r.date, t.id as time_id, t.start_at, m.id as theme_id, m.name as theme_name, m.description, m.url  "
                        +
                        "FROM reservation r " +
                        "INNER JOIN reservation_time t ON r.time_id = t.id " +
                        "INNER JOIN theme m ON r.theme_id = m.id " +
                        "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(sql, reservationMapper, id);
    }

    public int existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
    }
}
