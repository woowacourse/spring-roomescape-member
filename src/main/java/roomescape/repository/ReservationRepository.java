package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (
            resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("reservation_time_id"),
                    resultSet.getTime("time_value").toLocalTime()),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            )
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT " +
                "r.id AS reservation_id, " +
                "r.name, " +
                "r.date, " +
                "t.id AS reservation_time_id, " +
                "t.start_at AS time_value, " +
                "th.id AS theme_id, " +
                "th.name AS theme_name, " +
                "th.description AS theme_description, " +
                "th.thumbnail AS theme_thumbnail " +
                "FROM reservation AS r " +
                "INNER JOIN reservation_time AS t " +
                "ON r.reservation_time_id = t.id " +
                "INNER JOIN theme as th " +
                "ON r.theme_id = th.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, reservation_time_id, theme_id) " +
                "VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getReservationTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return new Reservation(keyHolder.getKey().longValue(), reservation.getName(),
                reservation.getDate(), reservation.getReservationTime(), reservation.getTheme());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation " +
                "WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existsByReservationTimeId(Long reservationTimeId) {
        String sql = "SELECT EXISTS (" +
                "SELECT 1 " +
                "FROM reservation " +
                "WHERE reservation_time_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, reservationTimeId);
    }

    public boolean existsByReservationThemeId(Long themeId) {
        String sql = "SELECT EXISTS (" +
                "SELECT 1 FROM reservation " +
                "WHERE theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, themeId);
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long reservationTimeId, Long themeId) {
        String sql = "SELECT EXISTS (" +
                "SELECT 1 " +
                "FROM reservation " +
                "WHERE date = ? AND reservation_time_id = ? AND theme_id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, Date.valueOf(date), reservationTimeId, themeId);
    }
}
