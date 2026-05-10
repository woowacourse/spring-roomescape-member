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

    public long save(Reservation reservation) {
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    """
                    MERGE INTO reservation r
                    USING (
                        VALUES (?, ?, ?, ?, ?)
                    ) t(id, name, date, time_id, theme_id)
                    ON r.id = t.id
                    WHEN MATCHED THEN
                        UPDATE SET
                            name = t.name,
                            date = t.date,
                            time_id = t.time_id,
                            theme_id = t.theme_id
                    WHEN NOT MATCHED THEN
                        INSERT (name, date, time_id, theme_id)
                        VALUES (t.name, t.date, t.time_id, t.theme_id)
                    """,
                    new String[]{"id"}
            );

            ps.setObject(1, reservation.getId());
            ps.setString(2, reservation.getName());
            ps.setObject(3, reservation.getDate());
            ps.setLong(4, reservation.getTime().getId());
            ps.setLong(5, reservation.getTheme().getId());

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            return keyHolder.getKey().longValue();
        }

        return reservation.getId();
    }

    public boolean existsByTimeId(long timeId) {
        final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    public void remove(long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public List<Reservation> findByName(String username) {
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
