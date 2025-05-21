package roomescape.dao.reservationtime;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.reservationtime.InvalidReservationTimeException;
import roomescape.domain.reservationtime.ReservationTime;

@Repository
public class JdbcReservationTimeDaoImpl implements ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationTimeDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        final String query = "select * from reservation_time";
        return jdbcTemplate.query(query,
                (resultSet, rowNum) -> new ReservationTime(
                        resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at"))));
    }

    @Override
    public void saveReservationTime(final ReservationTime reservationTime) {
        final Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("start_at", reservationTime.getStartAt());
        final Number newId = insertActor.executeAndReturnKey(parameters);
        reservationTime.setId(newId.longValue());
    }

    @Override
    public void deleteReservationTime(final Long id) {
        final String query = "delete from reservation_time where id = ?";
        try {
            final int deletedRowCount = jdbcTemplate.update(query, id);
            validateDeleteRowCount(deletedRowCount);
        } catch (final DataIntegrityViolationException e) {
            throw new InvalidReservationTimeException("삭제하려는 시간은 이미 예약 되어있는 시간 입니다.");
        }
    }

    private void validateDeleteRowCount(final int deletedRowCount) {
        if (deletedRowCount == 0) {
            throw new InvalidReservationTimeException("삭제하려는 ID의 시간이 존재하지 않습니다.");
        }
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        final String query = "select * from reservation_time where id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query,
                (resultSet, rowNum) -> new ReservationTime(
                        resultSet.getLong("id"),
                        LocalTime.parse(resultSet.getString("start_at"))), id));
    }
}
