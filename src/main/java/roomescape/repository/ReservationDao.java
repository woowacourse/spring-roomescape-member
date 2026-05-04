package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id AS r_id, r.name, r.date, t.id AS t_id, t.start_at " +
                "FROM reservation r INNER JOIN reservation_time t ON r.time_id = t.id";
        return jdbcTemplate.query(sql, rowMapper());
    }

    @Override
    public Reservation findById(long reservationId) {
        String sql = "SELECT r.id AS r_id, r.name, r.date, t.id AS t_id, t.start_at " +
                "FROM reservation r INNER JOIN reservation_time t ON r.time_id = t.id " +
                "WHERE r.id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper(), reservationId);
    }

    @Override
    public Reservation save(Reservation reservation) {
        SimpleJdbcInsert insert = createInsert();
        Map<String, Object> params = createParams(reservation);
        long reservationId = insert.executeAndReturnKey(params).longValue();
        return new Reservation(reservationId, reservation.name(), reservation.date(), reservation.reservationTime());
    }

    private SimpleJdbcInsert createInsert() {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    private Map<String, Object> createParams(Reservation reservation) {
        return Map.of("name", reservation.name(), "date", reservation.date(), "time_id", reservation.reservationTime().id());
    }

    @Override
    public void deleteById(long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, reservationId);
    }

    private RowMapper<Reservation> rowMapper() {
        return (rs, rowNum) -> new Reservation(
                rs.getLong("r_id"),
                rs.getString("name"),
                rs.getObject("date", LocalDate.class),
                new ReservationTime(rs.getLong("t_id"), rs.getObject("start_at", LocalTime.class))
        );
    }
}
