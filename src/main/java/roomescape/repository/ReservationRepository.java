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

    private static final RowMapper<Reservation> reservationMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
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
        String selectSql = "SELECT r.id, r.name, r.date, t.id as time_id, t.start_at, m.id as theme_id, m.name as theme_name, m.description, m.url  " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id " +
                "INNER JOIN theme m ON r.theme_id = m.id ";
        return jdbcTemplate.query(selectSql, reservationMapper);
    }

    public void removeById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Reservation register(ReservationRequest reservationRequest, TimeResponse timeResponse, ThemeResponse themeResponse) {
        String sql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationRequest.getName());
            ps.setObject(2, reservationRequest.getDate());
            ps.setLong(3, reservationRequest.getTimeId());
            ps.setLong(4, reservationRequest.getThemeId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Reservation(id, reservationRequest.getName(), reservationRequest.getDate(), ReservationTime.from(timeResponse), Theme.from(themeResponse));
    }
}
