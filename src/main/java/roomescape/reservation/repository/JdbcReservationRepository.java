package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.exception.ReservationNotFoundException;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String SELECT_RESERVATION_WITH_TIME = """
            SELECT
                r.id AS reservation_id,
                r.name,
                r.date,
                t.id AS time_id,
                t.start_at
            FROM reservation r
            INNER JOIN reservation_time t
                ON r.time_id = t.id
            """;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) ->
            Reservation.of(
                    rs.getLong("reservation_id"),
                    rs.getString("name"),
                    rs.getObject("date", LocalDate.class),
                    ReservationTime.of(
                            rs.getLong("time_id"),
                            rs.getObject("start_at", LocalTime.class)
                    )
            );

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(String name, LocalDate date, Long timeId) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setObject(2, date);
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = SELECT_RESERVATION_WITH_TIME + "WHERE r.id = ? ";
        List<Reservation> result = jdbcTemplate.query(sql, reservationRowMapper, id);

        return result.stream().findFirst();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = SELECT_RESERVATION_WITH_TIME + "ORDER BY r.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";

        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new ReservationNotFoundException(id);
        }
    }

}
