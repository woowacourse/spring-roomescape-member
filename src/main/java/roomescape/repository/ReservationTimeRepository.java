package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InUseTimeException;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime persist(ReservationTime reservationTime) {
        Number id = simpleJdbcInsert.executeAndReturnKey(Map.of(
                "start_at", reservationTime.getStartAt()
        ));

        return reservationTime.with(id.longValue());
    }

    public List<ReservationTime> findAll() {
        String findSql = "SELECT *"
                + " FROM reservation_time";

        return jdbcTemplate.query(findSql, reservationTimeRowMapper());
    }

    public Optional<ReservationTime> findById(long id) {
        try {
            String findSql = "SELECT id, start_at"
                    + " FROM reservation_time"
                    + " WHERE id = ?";

            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    findSql,
                    reservationTimeRowMapper(),
                    id
            );
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean delete(long id) {
        String deleteSql = "DELETE FROM reservation_time"
                + " WHERE id = ?";

        try {
            int deletedRowCount = jdbcTemplate.update(deleteSql, id);

            return isDeleted(deletedRowCount);
        } catch (DataIntegrityViolationException exception) {
            throw new InUseTimeException("사용중이지 않은 시간만 제거할 수 있습니다. id = " + id);
        }
    }

    private boolean isDeleted(int deletedCount) {
        return deletedCount > 0;
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            long id = resultSet.getLong("id");
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);

            return ReservationTime.retrieve(id, startAt);
        };
    }
}
