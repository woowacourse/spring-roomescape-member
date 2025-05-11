package roomescape.domain.reservationtime.dao;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.model.ReservationTime;

@Repository
public class JdbcReservationTimeDaoImpl implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationTimeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
            .withTableName("reservation_time")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = "select * from reservation_time";
        return jdbcTemplate.query(query,
            (resultSet, RowNum) -> {
                ReservationTime reservationTime = new ReservationTime(
                    resultSet.getLong("id"),
                    LocalTime.parse(resultSet.getString("start_at")));
                return reservationTime;
            });
    }

    @Override
    public long save(ReservationTime reservationTime) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", reservationTime.getStartAt());
        Number newId = insertActor.executeAndReturnKey(parameters);
        return newId.longValue();
    }

    @Override
    public boolean delete(Long id) {
        String query = "delete from reservation_time where id = ?";
        return jdbcTemplate.update(query, id) > 0;
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String query = "select * from reservation_time where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> {
                    return new ReservationTime(
                        resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at")));
                }, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findBookedTimes(String date, Long themeId) {
        String query = """
            select rt.id, rt.start_at 
            from reservation_time rt
            inner join reservation r on rt.id = r.time_id
            where r.date = ? AND r.theme_id = ?
            """;
        return jdbcTemplate.query(query,
            (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
            ), date, themeId);
    }
}
