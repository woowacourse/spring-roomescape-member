package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String SELECT_RESERVATION_WITH_TIME = """
            SELECT
                r.id AS reservation_id,
                r.name AS reservation_name,
                r.date,
                rt.id AS time_id,
                rt.start_at,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description,
                t.thumbnail_url,
                t.runtime
            FROM reservation r
            INNER JOIN reservation_time rt
                ON r.time_id = rt.id
            INNER JOIN theme t
                ON r.theme_id = t.id
            """;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) ->
            Reservation.of(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    rs.getObject("date", LocalDate.class),
                    ReservationTime.of(
                            rs.getLong("time_id"),
                            rs.getObject("start_at", LocalTime.class)
                    ),
                    Theme.of(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail_url"),
                            Duration.ofHours(rs.getLong("runtime"))
                    )
            );

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, reservation.getName());
                ps.setObject(2, reservation.getDate());
                ps.setLong(3, reservation.getTime().getId());
                ps.setLong(4, reservation.getTheme().getId());
                return ps;
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new ReservationDuplicatedException(
                    reservation.getDate(),
                    reservation.getTime().getId(),
                    reservation.getTheme().getId()
            );
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Reservation.of(
                id,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = SELECT_RESERVATION_WITH_TIME + "WHERE r.id = ?";

        List<Reservation> result = jdbcTemplate.query(sql, reservationRowMapper, id);

        return result.stream().findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = SELECT_RESERVATION_WITH_TIME + "ORDER BY r.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public List<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                SELECT time_id
                FROM reservation
                WHERE date = ? AND theme_id = ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getLong("time_id"),
                date,
                themeId
        );
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
