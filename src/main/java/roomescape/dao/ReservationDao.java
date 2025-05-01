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
import roomescape.model.Theme;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static RowMapper<Reservation> reservationRowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                rs.getDate("date").toLocalDate(),
                new ReservationTime(
                        rs.getLong("time_id"),
                        rs.getTime("time_value").toLocalTime()
                ),
                new Theme(
                        rs.getLong("theme_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail")
                )
        );
    }

    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO reservation(name, date, time_id, theme_id) VALUES(?, ?, ?, ?)",
                            new String[]{"id"});
                    ps.setString(1, reservation.getName());
                    ps.setDate(2, Date.valueOf(reservation.getDate()));
                    ps.setLong(3, reservation.getReservationTime().getId());
                    ps.setLong(4, reservation.getTheme().getId());
                    return ps;
                }
                , keyHolder);

        Long id = keyHolder.getKey().longValue();

        return new Reservation(
                id,
                reservation.getName(),
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

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t
                ON r.time_id = t.id
                INNER JOIN theme as th
                ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(
                sql,
                reservationRowMapper()
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
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name,
                    th.description,
                    th.thumbnail
                FROM reservation as r
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
}
