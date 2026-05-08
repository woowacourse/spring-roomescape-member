package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT\n" +
                "    r.id as reservation_id,\n" +
                "    r.name as username,\n" +
                "    r.date,\n" +
                "    rt.id as time_id,\n" +
                "    rt.start_at as time_value,\n" +
                "    t.id as theme_id,\n" +
                "    t.name as theme_name,\n" +
                "    t.description,\n" +
                "    t.thumbnail\n" +
                "FROM reservation as r\n" +
                "INNER JOIN reservation_time as rt\n" +
                "  ON r.time_id = rt.id\n" +
                "INNER JOIN theme as t\n" +
                "  ON r.theme_id = t.id\n";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT\n" +
                "    r.id as reservation_id,\n" +
                "    r.name as username,\n" +
                "    r.date,\n" +
                "    rt.id as time_id,\n" +
                "    rt.start_at as time_value,\n" +
                "    t.id as theme_id,\n" +
                "    t.name as theme_name,\n" +
                "    t.description,\n" +
                "    t.thumbnail\n" +
                "FROM reservation as r\n" +
                "INNER JOIN reservation_time as rt\n" +
                "  ON r.time_id = rt.id\n" +
                "INNER JOIN theme as t\n" +
                "  ON r.theme_id = t.id\n" +
                "WHERE r.id = ?";
        List<Reservation> result = jdbcTemplate.query(sql, reservationRowMapper, id);
        return result.stream().findAny();
    }

    public Long insert(Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            pstmt.setString(1, reservation.getName());
            pstmt.setObject(2, reservation.getDate());
            pstmt.setLong(3, reservation.getTime().getId());
            pstmt.setLong(4, reservation.getTheme().getId());
            return pstmt;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int delete(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findReservationsByThemeAndDate(Long themeId, LocalDate date) {
        String sql = "SELECT\n" +
                "    r.id as reservation_id,\n" +
                "    r.name as username,\n" +
                "    r.date,\n" +
                "    rt.id as time_id,\n" +
                "    rt.start_at as time_value,\n" +
                "    t.id as theme_id,\n" +
                "    t.name as theme_name,\n" +
                "    t.description,\n" +
                "    t.thumbnail\n" +
                "FROM reservation as r\n" +
                "INNER JOIN reservation_time as rt\n" +
                "  ON r.time_id = rt.id\n" +
                "INNER JOIN theme as t\n" +
                "  ON r.theme_id = t.id\n" +
                "WHERE t.id = ? "
                + "AND r.date = ?";

        return jdbcTemplate.query(sql, reservationRowMapper, themeId, date);
    }

    public boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId) {
        String sql = "SELECT count(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return count != null && count > 0;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT count(*) FROM reservation WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT count(*) FROM reservation WHERE time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT count(*) FROM reservation WHERE theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, themeId);
        return count != null && count > 0;
    }

    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("time_value", LocalTime.class));
        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail"));

        Reservation reservation = new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("username"),
                resultSet.getObject("date", LocalDate.class),
                time,
                theme);
        return reservation;
    };
}
