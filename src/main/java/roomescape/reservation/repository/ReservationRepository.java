package roomescape.reservation.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll(int page, int size) {
        String sql = """
                SELECT r.id          AS reservation_id,
                       r.name        AS reservation_name,
                       r.date        AS reservation_date,
                       t.id          AS time_id,
                       t.start_at    AS time_start_at,
                       th.id    AS theme_id,
                       th.name    AS theme_name,
                       th.description    AS theme_description,
                       th.thumbnail    AS theme_thumbnail
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                ORDER BY r.id
                LIMIT ? OFFSET ?
                """;
        int offset = Math.max(page, 0) * size;
        return jdbcTemplate.query(sql, reservationRowsMapper(), size, offset);
    }

    public Optional<Reservation> findById(long id) {
        String sql = """
                SELECT r.id          AS reservation_id,
                       r.name        AS reservation_name,
                       r.date        AS reservation_date,
                       t.id          AS time_id,
                       t.start_at    AS time_start_at,
                       th.id         AS theme_id,
                       th.name       AS theme_name,
                       th.description AS theme_description,
                       th.thumbnail  AS theme_thumbnail
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, reservationRowsMapper(), id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Reservation> findByName(String name, int page, int size) {
        String sql = """
                SELECT r.id          AS reservation_id,
                       r.name        AS reservation_name,
                       r.date        AS reservation_date,
                       t.id          AS time_id,
                       t.start_at    AS time_start_at,
                       th.id    AS theme_id,
                       th.name    AS theme_name,
                       th.description    AS theme_description,
                       th.thumbnail    AS theme_thumbnail
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                JOIN theme th ON r.theme_id = th.id
                WHERE r.name = ?
                ORDER BY r.id
                LIMIT ? OFFSET ?
                """;
        int offset = Math.max(page, 0) * size;
        return jdbcTemplate.query(sql, reservationRowsMapper(), name, size, offset);
    }

    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId);
        return count != null && count > 0;
    }

    public boolean existsByDateAndTimeIdAndThemeIdAndIdNot(long id, LocalDate date, long timeId, long themeId) {
        String sql = """
                SELECT COUNT(*)
                FROM reservation
                WHERE date = ? AND time_id = ? AND theme_id = ? AND id <> ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, date, timeId, themeId, id);
        return count != null && count > 0;
    }

    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setObject(2, reservation.getDate());
            ps.setObject(3, reservation.getTime().getId());
            ps.setObject(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);
        long id = keyHolder.getKey().longValue();
        return new Reservation(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme());
    }

    public boolean existsByTimeId(long id) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE time_id = ?";
        Integer count =  jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET date = ?, time_id = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getId());
    }

    public int deleteById(long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int deleteByIdAndName(long id, String name) {
        String sql = "DELETE FROM reservation WHERE id = ? AND name = ?";
        return jdbcTemplate.update(sql, id, name);
    }

    private RowMapper<Reservation> reservationRowsMapper() {
        return (rs, rowNum) -> {
            ReservationTime time = new ReservationTime(
                    rs.getLong("time_id"),
                    rs.getObject("time_start_at", LocalTime.class)
            );

            Theme theme = new Theme(
                    rs.getLong("theme_id"),
                    rs.getString("theme_name"),
                    rs.getString("theme_description"),
                    rs.getString("thumbnail")
            );

            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    LocalDate.parse(rs.getString("reservation_date")),
                    time,
                    theme
            );
        };
    }
}
