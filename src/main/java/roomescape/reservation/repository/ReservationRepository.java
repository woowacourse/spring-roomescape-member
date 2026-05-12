package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.doamin.Theme;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        final ReservationTime time = new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime());
        final Theme theme = new Theme(rs.getLong("theme_id"), rs.getString("theme_name"), rs.getString("theme_description"),
                rs.getString("theme_thumbnail"));

        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                rs.getDate("date").toLocalDate(),
                time,
                theme
        );
    };

    public List<Reservation> findAll() {
        final String sql = """
                SELECT r.id AS reservation_id, r.name, r.date,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail_url AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            return insert(reservation);
        }

        return merge(reservation);
    }

    private Reservation merge(Reservation reservation) {
        final String sql = """
                UPDATE reservation
                SET name = ?,
                    date = ?,
                    time_id = ?,
                    theme_id = ?
                WHERE id = ?
                """;
        final int affectedRows = jdbcTemplate.update(
                sql,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId(),
                reservation.getId()
        );

        if (affectedRows == 0) {
            return insert(reservation);
        }

        return reservation;
    }

    private Reservation insert(Reservation reservation) {
        if (reservation.getId() != null) {
            final String sql = "INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(
                    sql,
                    reservation.getId(),
                    reservation.getName(),
                    reservation.getDate(),
                    reservation.getTime().getId(),
                    reservation.getTheme().getId()
            );
            return reservation;
        }

        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                    new String[]{"id"}
            );

            ps.setString(1, reservation.getName());
            ps.setObject(2, reservation.getDate());
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return new Reservation(
                    keyHolder.getKey().longValue(),
                    reservation.getName(),
                    reservation.getDate(),
                    reservation.getTime(),
                    reservation.getTheme()
            );
        }

        return reservation;
    }

    public boolean existsByTimeId(long timeId) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findAllByName(String username) {
        final String sql = """
                SELECT r.id AS reservation_id, r.name, r.date,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail_url AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.name = ?
                """;
        return jdbcTemplate.query(sql, reservationRowMapper, username);
    }

    public Optional<Reservation> findById(long id) {
        final String sql = """
                SELECT r.id AS reservation_id, r.name, r.date,
                       t.id AS time_id, t.start_at AS time_value,
                       th.id AS theme_id, th.name AS theme_name, th.description AS theme_description, th.thumbnail_url AS theme_thumbnail
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
