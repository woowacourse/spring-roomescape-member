package roomescape.repository;

import java.time.LocalTime;
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
                selectedReservation.getString("date"), time);
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInsert;

    public ReservationJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation save(final Reservation reservation) {
        final ReservationTime time = reservation.getTime();
        final String name = reservation.getName().getValue();
        final String date = reservation.getFormattedDate();
        final SqlParameterSource reservationParameters = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", time.getId());
        final Long savedReservationId = reservationInsert.executeAndReturnKey(reservationParameters).longValue();
        return new Reservation(savedReservationId, name, date, time);
    }

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

    public boolean deleteById(final Long id) {
        final int affectedRowCount = jdbcTemplate.update("DELETE FROM reservations WHERE id = ?", id);
        return affectedRowCount == 1;
    }
}
