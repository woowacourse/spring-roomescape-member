package roomescape.reservation.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.exception.SaveException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
@RequiredArgsConstructor
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getDate("date").toLocalDate(),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    ),
                    new Theme(
                            rs.getLong("theme_id"),
                            rs.getString("theme_name"),
                            rs.getString("description"),
                            rs.getString("thumbnail")
                    )
            );

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Reservation reservation) {
        String sql = "INSERT INTO reservations (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        if (rowAffected != 1) {
            throw new SaveException("예약 정보 저장에 실패했습니다.");
        }

        final Number key = keyHolder.getKey();

        return key.longValue();
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "DELETE FROM reservations WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsByDateAndStartAtAndThemeId(final LocalDate date, final LocalTime startAt,
                                                    final Long themeId) {
        final String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservations AS r
                    INNER JOIN reservation_times AS rt
                    ON r.time_id = rt.id
                    INNER JOIN themes AS th
                    ON r.theme_id = th.id
                    WHERE r.date = ? AND rt.start_at = ? AND th.id = ?
                )
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, startAt, themeId);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        final String sql = """
                SELECT EXISTS(
                    SELECT 1
                    FROM reservations AS r
                    WHERE r.date = ? AND r.time_id = ? AND r.theme_id = ?
                    LIMIT 1
                )
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.name AS name,
                    r.date AS date,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail
                FROM reservations AS r
                INNER JOIN reservation_times AS rt
                ON r.time_id = rt.id
                INNER JOIN themes AS th
                ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        final List<Reservation> reservations = jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, id);
        if (!reservations.isEmpty()) {
            return Optional.of(reservations.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.name AS name,
                    r.date AS date,
                    rt.id AS time_id,
                    rt.start_at AS start_at,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS description,
                    th.thumbnail AS thumbnail
                FROM reservations AS r
                INNER JOIN reservation_times AS rt
                ON r.time_id = rt.id
                INNER JOIN themes AS th
                ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }
}
