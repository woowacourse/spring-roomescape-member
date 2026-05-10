package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.exception.DataReferencedException;

@Repository
public class ReservationTimeRepository {

    private static final String FK_RESERVATION_TIME_ID = "fk_reservation_time_id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time");
    }

    public ReservationTime persist(ReservationTime reservationTime) {
        simpleJdbcInsert.execute(Map.of(
                "id", reservationTime.id().toString(),
                "start_at", reservationTime.startAt()
        ));

        return reservationTime;
    }

    public List<ReservationTime> findAll() {
        String findSql = "SELECT *"
                + " FROM reservation_time";

        return jdbcTemplate.query(findSql, reservationTimeRowMapper());
    }

    public Optional<ReservationTime> findById(UUID id) {
        try {
            String findSql = "SELECT id, start_at"
                    + " FROM reservation_time"
                    + " WHERE id = ?";

            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    findSql,
                    reservationTimeRowMapper(),
                    id.toString()
            );
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean delete(UUID timeId) {
        try {
            String deleteSql = "DELETE FROM reservation_time"
                    + " WHERE id = ?";
            int deletedRowCount = jdbcTemplate.update(deleteSql, timeId.toString());

            return isDeleted(deletedRowCount);
        } catch (DataIntegrityViolationException exception) {
            if (isForeignKeyViolation(exception)) {
                throw new DataReferencedException(
                        "외래키 제약조건으로 인해 삭제에 실패했습니다.",
                        "timeId = " + timeId,
                        exception
                );
            }

            throw exception;
        }
    }

    private boolean isForeignKeyViolation(DataIntegrityViolationException exception) {
        String message = exception.getMessage();

        return message != null &&
                message.toLowerCase().contains(FK_RESERVATION_TIME_ID);
    }

    private boolean isDeleted(int deletedCount) {
        return deletedCount > 0;
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);

            return new ReservationTime(id, startAt);
        };
    }
}
