package roomescape.reservation;

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
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("user_name"),
            new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            ),
            resultSet.getObject("date", LocalDate.class),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getObject("start_at", LocalTime.class)
            )
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation(user_name,theme_id, date, time_id) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement psmt = con.prepareStatement(sql, new String[]{"id"});
            psmt.setString(1, reservation.getUserName());
            psmt.setLong(2, reservation.getTheme().getId());
            psmt.setObject(3, reservation.getDate());
            psmt.setLong(4, reservation.getTime().getId());

            return psmt;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation.getUserName(), reservation.getTheme(), reservation.getDate(),
                reservation.getTime());
    }

    public List<Reservation> findAll() {
        String sql = "SELECT r.id, r.user_name, r.date, t.id as time_id, t.start_at, c.id as theme_id, " +
                "c.name as theme_name, c.description as theme_description, c.thumbnail as theme_thumbnail " +
                "FROM reservation r INNER JOIN reservation_time t ON r.time_id = t.id INNER JOIN theme c ON r.theme_id = c.id ";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id, r.user_name, r.date, t.id as time_id, t.start_at, c.id as theme_id, " +
                "c.name as theme_name, c.description as theme_description, c.thumbnail as theme_thumbnail " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id INNER JOIN theme c ON r.theme_id = c.id " +
                "WHERE r.id = ?";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);
        return reservations.stream().findFirst();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT EXISTS (SELECT * FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }
}
