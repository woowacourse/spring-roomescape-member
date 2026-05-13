package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Duration;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation");
    }

    public Reservation persist(Reservation reservation) {
        EntityId timeId = reservation.timeId();
        EntityId themeId = reservation.themeId();

        simpleJdbcInsert.execute(Map.of(
                "id", reservation.id().getValueAsUuid(),
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", timeId.getValueAsUuid(),
                "theme_id", themeId.getValueAsUuid()
        ));

        return reservation;
    }

    public List<Reservation> findAll() {
        String findSql = "SELECT id, name, date, time_id, theme_id"
                + " FROM reservation r";

        return jdbcTemplate.query(findSql, reservationRowMapper());
    }

    public List<Reservation> findBetweenDuration(Duration duration) {
        String findSql = "SELECT id, name, date, time_id, theme_id"
                + " FROM reservation r"
                + " WHERE date BETWEEN ? AND ?";

        return jdbcTemplate.query(
                findSql,
                reservationRowMapper(),
                duration.startDate(),
                duration.endDate()
        );
    }

    public List<Reservation> findByDateAndThemeId(LocalDate date, EntityId themeId) {
        String findSql = "SELECT id, name, date, time_id, theme_id"
                + " FROM reservation r"
                + " WHERE date = ? AND theme_id = ?";

        return jdbcTemplate.query(
                findSql,
                reservationRowMapper(),
                date,
                themeId.getValueAsUuid()
        );
    }

    public boolean existByTimeId(EntityId timeId) {
        String countSql = "SELECT count(*)"
                + " FROM reservation r"
                + " WHERE time_id = ?";

        Integer count = jdbcTemplate.queryForObject(
                countSql,
                Integer.class,
                timeId.getValueAsUuid()
        );

        return count != null
                && count > 0;
    }

    public boolean existByThemeId(EntityId themeId) {
        String countSql = "SELECT count(*)"
                + " FROM reservation r"
                + " WHERE theme_id = ?";

        Integer count = jdbcTemplate.queryForObject(
                countSql,
                Integer.class,
                themeId.getValueAsUuid()
        );

        return count != null
                && count > 0;
    }

    public boolean delete(EntityId reservationId) {
        String deleteSql = "DELETE FROM reservation"
                + " WHERE id = ?";

        int deletedRowCount = jdbcTemplate.update(deleteSql, reservationId.getValueAsUuid());

        return isDeleted(deletedRowCount);
    }

    private boolean isDeleted(int deletedRowCount) {
        return deletedRowCount > 0;
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                readEntityId(resultSet, "id"),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                readEntityId(resultSet, "time_id"),
                readEntityId(resultSet, "theme_id")
        );
    }

    private EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        UUID uuid = resultSet.getObject(column, UUID.class);

        return EntityId.fromUuid(uuid);
    }
}
