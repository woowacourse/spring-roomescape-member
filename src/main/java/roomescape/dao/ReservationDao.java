package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Role;
import roomescape.model.Theme;
import roomescape.model.User;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("reservation_id"),
                new User(
                        rs.getLong("user_id"),
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.toList(rs.getString("roles"))
                ),
                rs.getDate("date").toLocalDate(),
                new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("theme_name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }

    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO reservation(user_id, date, time_id, theme_id) VALUES(?, ?, ?, ?)",
                            new String[]{"id"});
                    ps.setLong(1, reservation.getUser().getId());
                    ps.setDate(2, Date.valueOf(reservation.getDate()));
                    ps.setLong(3, reservation.getReservationTime().getId());
                    ps.setLong(4, reservation.getTheme().getId());
                    return ps;
                }
                , keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Reservation(
                id,
                reservation.getUser(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme()
        );
    }

    public boolean deleteById(Long id) {
        int deletedRow = jdbcTemplate.update(
                "DELETE FROM reservation WHERE id = ?",
                id
        );
        return deletedRow == 1;
    }

    public List<Reservation> findReservationsWithPage(int startRowNumber, int endRowNumber) {
        String sql = """
                SELECT r.id as reservation_id,
                    u.id as user_id,
                    u.name as user_name,
                    u.email,
                    u.password,
                    u.roles,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM (
                    SELECT ROW_NUMBER() OVER() as row_num, *
                    FROM reservation
                ) as r
                INNER JOIN users as u
                ON r.user_id = u.id
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                WHERE r.row_num BETWEEN ? AND ?
                ORDER BY r.row_num
                """;
        return jdbcTemplate.query(
                sql,
                reservationRowMapper(),
                startRowNumber,
                endRowNumber
        );
    }

    public boolean isExistByTimeId(Long timeId) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = ?)",
                Boolean.class,
                timeId
        );
    }

    public boolean isExistByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE theme_id = ? AND time_id = ? AND date = ?)",
                Boolean.class,
                themeId,
                timeId,
                date
        );
    }

    public boolean isExistByThemeId(Long themeId) {
        return jdbcTemplate.queryForObject("SELECT EXISTS (SELECT 1 FROM RESERVATION WHERE theme_id = ?)",
                Boolean.class,
                themeId);
    }

    public List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = """
                SELECT r.id as reservation_id,
                    u.id as user_id,
                    u.name as user_name,
                    u.email,
                    u.password,
                    u.roles,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN users as u
                ON r.user_id = u.id
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                WHERE theme_id = ?
                    AND
                    date = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper(), themeId, date);
    }

    public int countTotalReservation() {
        String sql = """
                SELECT COUNT(*) FROM RESERVATION
                """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
