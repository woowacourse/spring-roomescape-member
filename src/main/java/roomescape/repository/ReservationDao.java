package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.User;
import roomescape.dto.CreateReservationRequest;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        User user = new User(
                resultSet.getLong("user_id"),
                resultSet.getString("user_name"),
                resultSet.getString("email")
        );

        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_image_url")
        );

        return new Reservation(
                resultSet.getLong("id"),
                user,
                theme,
                resultSet.getDate("date").toLocalDate(),
                time
        );
    };

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation findById(Long id) {
        String sql = """
                SELECT r.id, r.date,
                       u.id as user_id, u.name as user_name, u.email,
                       t.id as time_id, t.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail_image_url
                FROM reservation r
                JOIN users u ON r.user_id = u.id
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT r.id, r.date,
                       u.id as user_id, u.name as user_name, u.email,
                       t.id as time_id, t.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail_image_url
                FROM reservation r
                JOIN users u ON r.user_id = u.id
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT COUNT(1) FROM reservation WHERE time_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, timeId);
        return count != null && count > 0;
    }

    public List<Reservation> findAllByUserId(Long userId) {
        String sql = """
                SELECT r.id, r.date,
                       u.id as user_id, u.name as user_name, u.email,
                       t.id as time_id, t.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail_image_url
                FROM reservation r
                JOIN users u ON r.user_id = u.id
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                WHERE r.user_id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    public Long save(CreateReservationRequest request) {
        String sql = "INSERT INTO reservation(user_id, theme_id, date, time_id) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, request.userId());
            ps.setLong(2, request.themeId());
            ps.setDate(3, Date.valueOf(request.date()));
            ps.setLong(4, request.timeId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updateDateAndTime(Long id, LocalDate date, Long timeId) {
        String sql = "UPDATE reservation SET date = ?, time_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, Date.valueOf(date), timeId, id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = """
                SELECT time_id
                FROM reservation
                WHERE theme_id = ? AND date = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("time_id"), themeId, date);
    }

    public boolean isExistsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = """
                SELECT COUNT(1) FROM reservation
                WHERE date = ? AND time_id = ? AND theme_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                Date.valueOf(date), timeId, themeId);
        return count != null && count > 0;
    }
}
