package roomescape.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime saveReservationTime(ReservationTime reservationTime) {
        Map<String, Object> parameters = Map.ofEntries(Map.entry("start_at", reservationTime.getStartAt()));
        long generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return ReservationTime.generateWithPrimaryKey(reservationTime, generatedKey);
    }

    public List<ReservationTime> readReservationTimes() {
        final String query = "SELECT id, start_at FROM reservation_time";
        List<ReservationTime> reservationTimes = jdbcTemplate.query(query,
                (resultSet, rowNum) -> new ReservationTime(resultSet.getLong("id"),
                        resultSet.getTime("start_at").toLocalTime()));

        return reservationTimes;
    }

    public Optional<ReservationTime> readReservationTime(Long timeId) {
        final String query = "SELECT id, start_at FROM reservation_time WHERE id = ?";

        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(query,
                            (resultSet, rowNum) -> new ReservationTime(resultSet.getLong("id"),
                            resultSet.getTime("start_at").toLocalTime()), timeId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteReservationTime(Long id) {
        final String query = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(query, id);
    }
}
