package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

import java.util.List;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private static final RowMapper<Reservation> ROW_MAPPER = (selectedReservation, rowNum) -> {
        final ReservationTime time = new ReservationTime(
                selectedReservation.getLong("time_id"),
                LocalTime.parse(selectedReservation.getString("start_at"))
        );
        return new Reservation(
                selectedReservation.getLong("id"),
                selectedReservation.getString("name"),
                LocalDate.parse(selectedReservation.getString("date")), time);
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
        final ReservationTime time = reservation.getTime();
        final String name = reservation.getName().getValue();
        final String date = reservation.getFormattedDate();
        final SqlParameterSource reservationParameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", time.getId());
        final Long savedReservationId = reservationInsert.executeAndReturnKey(reservationParameters).longValue();
        return new Reservation(savedReservationId, name, LocalDate.parse(date), time);
    }

    @Override
    public List<Reservation> findAll() {
        final String selectQuery = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at
            FROM reservations as r
            INNER JOIN reservation_times as t
            ON r.time_id = t.id
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
                t.id as time_id,
                t.start_at
            FROM reservations as r
            INNER JOIN reservation_times as t
            ON r.time_id = t.id
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
    public boolean existByDateAndTimeId(final LocalDate date, final Long timeId) {
        String sql = """
                SELECT 
                CASE WHEN EXISTS (
                        SELECT 1
                        FROM reservations
                        WHERE date = ? AND time_id = ?
                    )
                    THEN TRUE
                    ELSE FALSE
                END""";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId));
    }
}
