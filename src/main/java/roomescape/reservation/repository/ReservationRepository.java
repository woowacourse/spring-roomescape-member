package roomescape.reservation.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.model.Reservation;
import roomescape.schedule.model.Schedule;
import roomescape.theme.model.Theme;
import roomescape.user.model.Role;
import roomescape.user.model.User;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long create(Reservation reservation) {
        String sql = "INSERT INTO reservation (schedule_id, user_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                    ps.setLong(1, reservation.getSchedule().getId());
                    ps.setLong(2, reservation.getUser().getId());
                    return ps;
                }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id AS reservation_id, 
                       u.id AS user_id, 
                       u.name AS user_name, 
                       u.role AS user_role,
                       t.id AS theme_id, 
                       t.description AS theme_description,
                       t.image_url AS theme_image,         
                       t.required_time AS theme_required_time,
                       t.name AS theme_name,
                       s.id AS schedule_id, 
                       s.start_at AS start_at, 
                       s.end_at AS end_at
                FROM reservation r
                INNER JOIN "USER" u ON r.user_id = u.id
                INNER JOIN schedule s ON r.schedule_id = s.id
                INNER JOIN theme t ON s.theme_id = t.id
                """;


        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            User user = new User(
                    resultSet.getLong("user_id"),
                    resultSet.getString("user_name"),
                    Role.valueOf(resultSet.getString("user_role"))
            );

            Theme theme = new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("description"), resultSet.getString("image_url"),
                    resultSet.getObject("required_time", LocalTime.class));

            Schedule schedule = new Schedule(resultSet.getLong("schedule_id"),
                    resultSet.getObject("start_at", LocalDateTime.class),
                    theme);

            return new Reservation(resultSet.getLong("reservation_id"), user, schedule);
        });
    }

    public List<Reservation> findAllByUserId(Long userId) {
        String sql = """
                SELECT r.id AS reservation_id,
                       u.id AS user_id,
                       u.name AS user_name,
                       u.role AS user_role,
                       t.id AS theme_id,
                       t.description AS theme_description,
                       t.image_url AS theme_image,
                       t.required_time AS theme_required_time,
                       t.name AS theme_name,
                       s.id AS schedule_id,
                       s.start_at AS start_at,
                       s.end_at AS end_at
                FROM reservation r
                INNER JOIN "USER" u ON r.user_id = u.id
                INNER JOIN schedule s ON r.schedule_id = s.id
                INNER JOIN theme t ON s.theme_id = t.id
                WHERE u.id = ?
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            User user = new User(
                    resultSet.getLong("user_id"),
                    resultSet.getString("user_name"),
                    Role.valueOf(resultSet.getString("user_role"))
            );
            Theme theme = new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                    resultSet.getString("description"), resultSet.getString("image_url"),
                    resultSet.getObject("required_time", LocalTime.class));
            Schedule schedule = new Schedule(resultSet.getLong("schedule_id"),
                    resultSet.getObject("start_at", LocalDateTime.class),
                    theme);
            return new Reservation(resultSet.getLong("reservation_id"), user, schedule);
        }, userId);
    }

    public boolean existsByScheduleId(Long scheduleId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE schedule_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, scheduleId);
    }

    public int delete(Long id) {
        String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT r.id AS reservation_id,
                       u.id AS user_id,
                       u.name AS user_name,
                       u.role AS user_role,
                       t.id AS theme_id,
                       t.description AS theme_description,
                       t.image_url AS theme_image,
                       t.required_time AS theme_required_time,
                       t.name AS theme_name,
                       s.id AS schedule_id,
                       s.start_at AS start_at,
                       s.end_at AS end_at
                FROM reservation r
                INNER JOIN "USER" u ON r.user_id = u.id
                INNER JOIN schedule s ON r.schedule_id = s.id
                INNER JOIN theme t ON s.theme_id = t.id
                WHERE r.id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
                User user = new User(
                        resultSet.getLong("user_id"),
                        resultSet.getString("user_name"),
                        Role.valueOf(resultSet.getString("user_role"))
                );
                Theme theme = new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                        resultSet.getString("description"), resultSet.getString("image_url"),
                        resultSet.getObject("required_time", LocalTime.class));
                Schedule schedule = new Schedule(resultSet.getLong("schedule_id"),
                        resultSet.getObject("start_at", LocalDateTime.class),
                        theme);
                return new Reservation(resultSet.getLong("reservation_id"), user, schedule);
            }, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int update(Long reservationId, Long newScheduleId, Long userId) {
        String sql = "UPDATE reservation SET schedule_id = ? WHERE id = ? AND user_id = ?";
        return jdbcTemplate.update(sql, newScheduleId, reservationId, userId);
    }
}
