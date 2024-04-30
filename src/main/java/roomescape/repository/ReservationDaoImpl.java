package roomescape.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(resultSet.getLong("time_id"), resultSet.getTime("time_value").toLocalTime())
    );

    public ReservationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT"
                + "    r.id as reservation_id,"
                + "    r.name,"
                + "    r.date,"
                + "    t.id as time_id,"
                + "    t.start_at as time_value "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time as t "
                + "ON r.time_id = t.id ";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = "SELECT"
                + "    r.id as reservation_id,"
                + "    r.name,"
                + "    r.date,"
                + "    t.id as time_id,"
                + "    t.start_at as time_value "
                + "FROM reservation as r "
                + "INNER JOIN reservation_time as t "
                + "ON r.time_id = t.id "
                + "WHERE r.id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    @Override
    public Reservation insert(Reservation reservation) {
        Map<String, Object> reservationRow = new HashMap<>();
        reservationRow.put("name", reservation.getName());
        reservationRow.put("date", reservation.getDate());
        reservationRow.put("time_id", reservation.getTimeId());
        Long id = simpleJdbcInsert.executeAndReturnKey(reservationRow).longValue();
        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }
}
