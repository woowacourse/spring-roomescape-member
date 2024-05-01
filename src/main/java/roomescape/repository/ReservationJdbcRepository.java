package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (selectedReservation, rowNum) -> {
        final ReservationTime time = new ReservationTime(
                selectedReservation.getLong("time_id"),
                LocalTime.parse(selectedReservation.getString("start_at"))
        );
        final Theme theme = new Theme(
                selectedReservation.getLong("theme_id"),
                selectedReservation.getString("theme_name"),
                selectedReservation.getString("description"),
                selectedReservation.getString("thumbnail")
        );

        return new Reservation(
                selectedReservation.getLong("id"),
                selectedReservation.getString("name"),
                LocalDate.parse(selectedReservation.getString("date")),
                time,
                theme
        );
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInsert;

    public ReservationJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final BeanPropertySqlParameterSource reservationParameters = new BeanPropertySqlParameterSource(reservation);
        final Long savedReservationId = reservationInsert.executeAndReturnKey(reservationParameters).longValue();
        return new Reservation(
                savedReservationId,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        final String selectQuery = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                rt.id as time_id,
                rt.start_at,
                t.id as theme_id,
                t.name as theme_name,
                t.description,
                t.thumbnail
            FROM reservations as r
            INNER JOIN reservation_times as rt
            ON r.time_id = rt.id
            INNER JOIN theme as t
            ON r.theme_id = t.id
        """;
        return jdbcTemplate.query(selectQuery, ROW_MAPPER)
                .stream()
                .toList();
    }

    @Override
    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM reservations WHERE id = ?", id);
    }

    @Override
    public boolean existByTimeId(final Long timeId) {
        String sql = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE time_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, timeId));
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String selectQuery = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                rt.id as time_id,
                rt.start_at,
                t.id as theme_id,
                t.name as theme_name,
                t.description,
                t.thumbnail
            FROM reservations as r
            INNER JOIN reservation_times as rt
            ON r.time_id = rt.id
            INNER JOIN theme as t
            ON r.theme_id = t.id
            WHERE r.id = ?
        """;

        try {
            final Reservation reservation = jdbcTemplate.queryForObject(selectQuery, ROW_MAPPER, id);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        String sql = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE date = ? AND time_id = ? AND theme_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId));
    }

    @Override
    public boolean existByThemeId(final Long themeId) {
        String sql = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE theme_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, themeId));
    }
}
