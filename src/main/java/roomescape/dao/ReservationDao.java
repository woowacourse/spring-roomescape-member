package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> actorRowMapper = (resultSet, rowNum) -> {
        Reservation reservation = new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(resultSet.getLong("time_id"))
        );
        return reservation;
    };

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value
                FROM reservation r
                INNER JOIN reservation_time t
                    ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public Long saveReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTimeId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Reservation> findByDateAndTime(Reservation reservation) {
        String sql = "SELECT id, name, date, time_id FROM reservation WHERE date = ? AND time_id = ?";
        return jdbcTemplate.query(sql, actorRowMapper, reservation.getDate(), reservation.getTimeId())
                .stream()
                .findFirst();
    }
}
