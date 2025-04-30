package roomescape.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationDaoImpl implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("reservation")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAllReservation() {
        String sql = "select r.id as reservation_id, r.name, r.date, t.id as time_id,  t.start_at as time_value from reservation as r inner join reservation_time as t on r.time_id = t. id";
        return jdbcTemplate.query(sql,
            (resultSet, RowNum) -> {
                Reservation reservation = new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Person(resultSet.getString("name")),
                    new ReservationDate(LocalDate.parse(resultSet.getString("date"))),
                    new ReservationTime(
                        resultSet.getLong("time_id"),
                        LocalTime.parse(resultSet.getString("time_value")))
                );
                return reservation;
            });
    }

    @Override
    public void saveReservation(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", reservation.getPersonName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTimeId());
        Number newId = insertActor.executeAndReturnKey(parameters);
        reservation.setId(newId.longValue());
    }

    @Override
    public void deleteReservation(Long id) {
        String query = "delete from reservation where id = ?";
        jdbcTemplate.update(query, id);
    }

    @Override
    public int findByTimeId(Long id) {
        String query = "select count(*) from reservation where time_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, id);
    }
}
