package roomescape.dao.jdbc;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAllReservations() {
        String sql = """
            SELECT
                r.id as reservation_id,
                r.name,
                r.date,
                t.id as time_id,
                t.start_at as time_value
            FROM reservation as r
            INNER JOIN reservation_time as t
            ON r.time_id = t.id
            """;

        return jdbcTemplate.query(sql, createReservationMapper());
    }

    public Reservation addReservation(Reservation reservation) {
        Map<String, Object> param = new HashMap<>();
        param.put("name", reservation.getName());
        param.put("date", Date.valueOf(reservation.getDate()));
        param.put("time_id", reservation.getTime().getId());

        Number key = jdbcInsert.executeAndReturnKey(param);

        return new Reservation(key.longValue(), reservation.getName(), reservation.getDate(),
            reservation.getTime());
    }

    public boolean existReservationByDateAndTime(LocalDate date, Long timeId) {
        String sql = "SELECT EXISTS(SELECT id FROM reservation WHERE date = ? AND time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId);
    }

    public void removeReservationById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<Reservation> createReservationMapper() {
        return (rs, rowNum) -> new Reservation(
            rs.getLong("reservation_id"),
            rs.getString("name"),
            rs.getDate("date").toLocalDate(),
            new ReservationTime(
                rs.getLong("time_id"),
                rs.getTime("time_value").toLocalTime())
        );
    }
}
