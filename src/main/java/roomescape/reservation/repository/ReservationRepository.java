package roomescape.reservation.repository;

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

    public void delete(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
