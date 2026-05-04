package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAllWithTime() {
        String sql = """
                SELECT r.id          AS reservation_id,
                       r.name        AS reservation_name,
                       r.date        AS reservation_date,
                       t.id          AS time_id,
                       t.start_at    AS time_start_at
                FROM reservation r
                JOIN reservation_time t ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationRowsMapper());
    }

    public Long save(String name, LocalDate date, Long timeId) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setObject(2, date);
            ps.setObject(3, timeId);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private RowMapper<Reservation> reservationRowsMapper() {
        return (rs, rowNum) -> {
            ReservationTime time = new ReservationTime(
                    rs.getLong("time_id"),
                    LocalTime.parse(rs.getString("time_start_at"))
            );
            return new Reservation(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    LocalDate.parse(rs.getString("reservation_date")),
                    time
            );
        };
    }
}