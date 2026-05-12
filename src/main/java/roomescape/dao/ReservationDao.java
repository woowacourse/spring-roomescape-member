package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> Reservation.restore(
            rs.getLong("reservation_id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            rs.getTimestamp("created_at").toLocalDateTime(),
            new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime()),
            new Theme(rs.getLong("theme_id"), rs.getString("theme_name"), rs.getString("theme_description"),
                    rs.getString("theme_thumbnail"))
    );

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        final String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, r.created_at,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail_url AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Optional<Reservation> findById(long id) {
        final String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, r.created_at,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail_url AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, id).stream().findFirst();
    }

    public Reservation save(Reservation reservation) {
        final long id = jdbcInsert.executeAndReturnKey(Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "created_at", reservation.getCreatedAt(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId()
        )).longValue();
        return Reservation.restore(id, reservation.getName(), reservation.getDate(),
                reservation.getCreatedAt(), reservation.getTime(), reservation.getTheme());
    }

    public boolean existsByTimeId(long timeId) {
        final Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE time_id = ?", Integer.class, timeId);
        return count > 0;
    }

    public boolean existsByThemeId(long themeId) {
        final Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE theme_id = ?", Integer.class, themeId);
        return count > 0;
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        final Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?",
                Integer.class, date, timeId, themeId);
        return count > 0;
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findByName(String username) {
        final String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, r.created_at,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail_url AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.name = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, username);
    }
}