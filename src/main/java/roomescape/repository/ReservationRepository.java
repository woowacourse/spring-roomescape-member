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
public class ReservationRepository {

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
    private static final RowMapper<Theme> THEME_ROW_MAPPER = (selectedTheme, rowNum) -> new Theme(
            selectedTheme.getLong("id"),
            selectedTheme.getString("name"),
            selectedTheme.getString("description"),
            selectedTheme.getString("thumbnail"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInsert;

    public ReservationRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id");
    }

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

    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themId) {
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
            WHERE r.date = ? AND r.themId = ?
        """;
        return jdbcTemplate.query(selectQuery, ROW_MAPPER, date, themId)
                .stream()
                .toList();
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM reservations WHERE id = ?", id);
    }

    public boolean existByTimeId(final Long timeId) {
        final String selectQuery = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE time_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(selectQuery, Boolean.class, timeId));
    }

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

    public boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        final String selectQuery = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE date = ? AND time_id = ? AND theme_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(selectQuery, Boolean.class, date, timeId, themeId));
    }

    public boolean existByThemeId(final Long themeId) {
        final String selectQuery = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE theme_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(selectQuery, Boolean.class, themeId));
    }

    public List<Theme> findThemesOrderedByReservationCountForWeek(final LocalDate localDate, final int count) {
        final String selectQuery = """
                SELECT t.id, t.name, t.description, t.thumbnail, COUNT(t.id) AS count
                FROM reservations AS r
                LEFT JOIN theme AS t
                ON t.id = r.theme_id
                WHERE r.date <= ? AND r.date > ?
                GROUP BY t.id
                ORDER BY count DESC
                LIMIT ?
                """;

        return jdbcTemplate.query(selectQuery, THEME_ROW_MAPPER, localDate, localDate.minusWeeks(1), count);
    }
}
