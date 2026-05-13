package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.Reservation;
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

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
            ),
            resultSet.getObject("deleted_at", LocalDateTime.class)
    );

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation(user_name,theme_id, date, time_id, deleted_at) VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement psmt = con.prepareStatement(sql, new String[]{"id"});
            psmt.setString(1, reservation.getUserName());
            psmt.setLong(2, reservation.getTheme().getId());
            psmt.setObject(3, reservation.getDate());
            psmt.setLong(4, reservation.getTime().getId());
            psmt.setObject(5, reservation.getDeletedAt());
            return psmt;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Reservation(id, reservation.getUserName(), reservation.getTheme(), reservation.getDate(),
                reservation.getTime(), reservation.getDeletedAt());
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id, r.user_name, r.date, r.deleted_at, t.id as time_id, t.start_at, c.id as theme_id, " +
                "c.name as theme_name, c.description as theme_description, c.thumbnail as theme_thumbnail " +
                "FROM reservation r INNER JOIN reservation_time t ON r.time_id = t.id " +
                "INNER JOIN theme c ON r.theme_id = c.id WHERE r.deleted_at IS NULL";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT r.id, r.user_name, r.date, r.deleted_at, t.id as time_id, t.start_at, c.id as theme_id, " +
                "c.name as theme_name, c.description as theme_description, c.thumbnail as theme_thumbnail " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id INNER JOIN theme c ON r.theme_id = c.id " +
                "WHERE r.id = ? AND r.deleted_at IS NULL";
        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, id);
        return reservations.stream().findFirst();
    }

    @Override
    public List<Reservation> findByUserName(String userName) {
        String sql = "SELECT r.id, r.user_name, r.date, r.deleted_at, t.id as time_id, t.start_at, c.id as theme_id, " +
                "c.name as theme_name, c.description as theme_description, c.thumbnail as theme_thumbnail " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id INNER JOIN theme c ON r.theme_id = c.id " +
                "WHERE r.user_name = ? AND r.deleted_at IS NULL";

        List<Reservation> reservations = jdbcTemplate.query(sql, reservationRowMapper, userName);
        return reservations;
    }

    @Override
    public List<Reservation> findByThemeAndDate(Long themeId, LocalDate date) {
        String sql = "SELECT r.id, r.user_name, r.date, r.deleted_at, t.id as time_id, t.start_at, c.id as theme_id, " +
                "c.name as theme_name, c.description as theme_description, c.thumbnail as theme_thumbnail " +
                "FROM reservation r " +
                "INNER JOIN reservation_time t ON r.time_id = t.id " +
                "INNER JOIN theme c ON r.theme_id = c.id " +
                "WHERE r.date = ? AND r.theme_id = ? AND r.deleted_at IS NULL";

        return jdbcTemplate.query(sql, reservationRowMapper, date, themeId);
    }

    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET theme_id = ?, date = ?, time_id = ?, deleted_at = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                reservation.getTheme().getId(),
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getDeletedAt(),
                reservation.getId()
        );
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT EXISTS (SELECT * FROM reservation WHERE time_id = ? AND deleted_at IS NULL)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public boolean existsActiveByDateAndThemeAndTime(LocalDate date, Long themeId, Long timeId) {
        String sql = "SELECT COUNT(*) FROM reservation " +
                "WHERE date = ? AND theme_id = ? AND time_id = ? AND deleted_at IS NULL";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, themeId, timeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsActiveByDateAndThemeAndTimeExcludingId(LocalDate date, Long themeId, Long timeId, Long excludeId) {
        String sql = "SELECT COUNT(*) FROM reservation " +
                "WHERE date = ? AND theme_id = ? AND time_id = ? AND deleted_at IS NULL AND id != ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, themeId, timeId, excludeId);
        return count != null && count > 0;
    }
}
