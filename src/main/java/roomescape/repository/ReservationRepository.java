package roomescape.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationRepository {
    public static final org.springframework.jdbc.core.RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) -> Reservation.of(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getString("date"),
            ReservationTime.of(resultSet.getLong("time_id"), resultSet.getString("start_at")));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<Reservation> findById(long reservationId) {
        String sql = "select r.id as reservation_id, r.name, r.date, rt.id as time_id, rt.start_at from reservation r inner join reservation_time rt on r.time_id = rt.id where r.id = ?";
        List<Reservation> result = jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, reservationId);
        return result.stream().findFirst();
    }

    public List<Reservation> findAll() {
        String sql = "select r.id as reservation_id, r.name, r.date, rt.id as time_id, rt.start_at from reservation r inner join reservation_time rt on r.time_id = rt.id";
        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> params = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate().getDate(),
                "time_id", reservation.getTime().getId()
        );

        long generatedKey = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Reservation.of(generatedKey, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
