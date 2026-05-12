package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.EntityId;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time");
    }

    public ReservationTime persist(ReservationTime reservationTime) {
        simpleJdbcInsert.execute(Map.of(
                "id", reservationTime.id().getValueAsUuid(),
                "start_at", reservationTime.startAt()
        ));

        return reservationTime;
    }

    public List<ReservationTime> findAll() {
        String findSql = "SELECT *"
                + " FROM reservation_time";

        return jdbcTemplate.query(findSql, reservationTimeRowMapper());
    }

    public Optional<ReservationTime> findById(EntityId id) {
        try {
            String findSql = "SELECT id, start_at"
                    + " FROM reservation_time"
                    + " WHERE id = ?";

            ReservationTime reservationTime = jdbcTemplate.queryForObject(
                    findSql,
                    reservationTimeRowMapper(),
                    id.getValueAsUuid()
            );
            return Optional.ofNullable(reservationTime);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public boolean delete(EntityId timeId) {
        String deleteSql = "DELETE FROM reservation_time"
                + " WHERE id = ?";
        int deletedRowCount = jdbcTemplate.update(deleteSql, timeId.getValueAsUuid());

        return isDeleted(deletedRowCount);
    }

    private boolean isDeleted(int deletedCount) {
        return deletedCount > 0;
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return (resultSet, rowNum) -> {
            EntityId id = readEntityId(resultSet, "id");
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);

            return new ReservationTime(id, startAt);
        };
    }

    private EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        UUID uuid = resultSet.getObject(column, UUID.class);

        return EntityId.fromString(uuid.toString());
    }
}
