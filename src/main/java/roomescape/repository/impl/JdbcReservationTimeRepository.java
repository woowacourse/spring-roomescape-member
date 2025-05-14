package roomescape.repository.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.NotAbleDeleteException;
import roomescape.common.mapper.ReservationTimeMapper;
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

    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> parameters = Map.ofEntries(Map.entry("start_at", reservationTime.getStartAt()));
        long generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();

        return ReservationTime.generateWithPrimaryKey(reservationTime, generatedKey);
    }

    public List<ReservationTime> readAll() {
        final String query = "SELECT id, start_at FROM reservation_time";
        List<ReservationTime> reservationTimes = jdbcTemplate.query(
                query,
                new ReservationTimeMapper()
        );

        return reservationTimes;
    }

    public Optional<ReservationTime> findById(Long timeId) {
        final String query = "SELECT id, start_at FROM reservation_time WHERE id = ?";

        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            query,
                            new ReservationTimeMapper(),
                            timeId
                    ));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(Long id) {
        final String query = "DELETE FROM reservation_time WHERE id = ?";
        int affectedRows = jdbcTemplate.update(query, id);
        if (affectedRows == 0) {
            throw new NotAbleDeleteException("예약 시간을 삭제할 수 없습니다. 존재하지 않는 ID입니다.");
        }
    }

    public List<ReservationTime> findAvailableTimesBy(LocalDate localDate, Long themeId) {
        final String query = """
                SELECT id, start_at
                FROM reservation_time
                WHERE id NOT IN (
                    SELECT rt.id
                    FROM reservation r
                    INNER JOIN reservation_time rt ON r.time_id = rt.id
                    WHERE r.date = ? AND r.theme_id = ?
                )
                """;

        List<ReservationTime> reservationTimes = jdbcTemplate.query(
                query,
                new ReservationTimeMapper(),
                localDate, themeId
        );

        return reservationTimes;
    }
}
