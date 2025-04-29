package roomescape.reservation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.Dao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;

@Repository
public class ReservationDao implements Dao<Reservation> {

    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Reservation add(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", reservation.name());
        parameters.put("date", reservation.date());
        parameters.put("time_id", reservation.time().id());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new Reservation(id, reservation.name(), reservation.date(), reservation.time());
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        // 미사용
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT r.id as reservation_id, r.name, r.date, "
                + "t.id AS time_id, "
                + "t.start_at AS time_value "
                + "FROM reservation AS r "
                + "INNER JOIN reservation_time AS t "
                + "ON r.time_id = t.id";

        return namedParameterJdbcTemplate.query(sql, (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getString("time_value")
                )
        ));
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        Map<String, Object> parameter = Map.of("id", id);

        namedParameterJdbcTemplate.update(sql, parameter);
    }
}
