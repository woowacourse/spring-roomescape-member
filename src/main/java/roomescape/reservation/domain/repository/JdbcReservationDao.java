package roomescape.reservation.domain.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

@Repository
public class JdbcReservationDao implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> reservationMapper =
            (rs, rowNum) -> {
                Long reservationId = rs.getLong("reservation_id");
                String name = rs.getString("name");
                LocalDate date = LocalDate.parse(rs.getString("date"));
                Long timeId = rs.getLong("time_id");
                LocalTime startAt = LocalTime.parse(rs.getString("start_at"));
                ReservationTime time = new ReservationTime(timeId, startAt);
                return new Reservation(reservationId, name, date, time);
            };

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, reservation.getName());
                    preparedStatement.setString(2, reservation.getDate().toString());
                    preparedStatement.setLong(3, reservation.getTime().getId());
                    return preparedStatement;
                },
                keyHolder
        );

        return keyHolder.getKey().longValue();
    }

    @Override
    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, reservationMapper);
    }

    @Override
    public List<Reservation> findAllByTimeId(Long timeId) {
        String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                WHERE t.id = ?
                """;
        return jdbcTemplate.query(sql, reservationMapper, timeId);
    }

    @Override
    public Boolean existByDateAndTimeId(LocalDate date, Long timeId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1
                    FROM reservation AS r
                    INNER JOIN reservation_time AS t
                    ON r.time_id = t.id
                    WHERE r.date = ? AND t.id = ?  
                );                  
                """;

        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId);
    }
}
