package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import roomescape.domain.Duration;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingColumns("id", "name", "date", "canceled", "time_id", "theme_id");
    }

    public Reservation persist(Reservation reservation) {
        EntityId timeId = reservation.getTimeId();
        EntityId themeId = reservation.getThemeId();

        simpleJdbcInsert.execute(Map.of(
                "id", reservation.getId().getValueAsUuid(),
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "canceled", reservation.isCanceled(),
                "time_id", timeId.getValueAsUuid(),
                "theme_id", themeId.getValueAsUuid()
        ));

        return reservation;
    }

    public List<Reservation> findAll() {
        String findSql = "SELECT r.id, r.name, r.date, r.canceled, r.time_id, rt.start_at, r.theme_id"
                + " FROM reservation r"
                + " JOIN reservation_time rt ON r.time_id = rt.id";

        return jdbcTemplate.query(findSql, reservationRowMapper());
    }

    public Optional<Reservation> findById(EntityId reservationId) {
        try {
            String findSql = "SELECT r.id, r.name, r.date, r.canceled, r.time_id, rt.start_at, r.theme_id"
                    + " FROM reservation r"
                    + " JOIN reservation_time rt ON r.time_id = rt.id"
                    + " WHERE r.id = ?";

            Reservation reservation = jdbcTemplate.queryForObject(
                    findSql,
                    reservationRowMapper(),
                    reservationId.getValueAsUuid()
            );

            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<Reservation> findByName(String name) {
        String findSql = "SELECT r.id, r.name, r.date, r.canceled, r.time_id, rt.start_at, r.theme_id"
                + " FROM reservation r"
                + " JOIN reservation_time rt ON r.time_id = rt.id"
                + " WHERE r.name = ?";

        return jdbcTemplate.query(
                findSql,
                reservationRowMapper(),
                name
        );
    }

    public List<Reservation> findBetweenDuration(Duration duration) {
        String findSql = "SELECT r.id, r.name, r.date, r.canceled, r.time_id, rt.start_at, r.theme_id"
                + " FROM reservation r"
                + " JOIN reservation_time rt ON r.time_id = rt.id"
                + " WHERE r.date BETWEEN ? AND ?";

        return jdbcTemplate.query(
                findSql,
                reservationRowMapper(),
                duration.startDate(),
                duration.endDate()
        );
    }

    public List<Reservation> findNotCanceledByDateAndThemeId(LocalDate date, EntityId themeId) {
        String findSql = "SELECT r.id, r.name, r.date, r.canceled, r.time_id, rt.start_at, r.theme_id"
                + " FROM reservation r"
                + " JOIN reservation_time rt ON r.time_id = rt.id"
                + " WHERE r.date = ? AND r.theme_id = ? AND r.canceled = false";

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

    public boolean existNotCanceledByDateAndThemeIdAndTimeId(
            LocalDate date,
            EntityId themeId,
            EntityId timeId
    ) {
        String countSql = "SELECT count(*)"
                + " FROM reservation"
                + " WHERE date = ? AND theme_id = ? AND time_id = ? AND canceled = false";

        Integer reservationCount = jdbcTemplate.queryForObject(
                countSql,
                Integer.class,
                date,
                themeId.getValueAsUuid(),
                timeId.getValueAsUuid()
        );

        return reservationCount != null
                && reservationCount > 0;
    }

    public Reservation updateDateAndTimeId(
            Reservation reservation,
            LocalDate date,
            ReservationTime time
    ) {
        String updateSql = "UPDATE reservation"
                + " SET date = ?, time_id = ?"
                + " WHERE id = ?";

        jdbcTemplate.update(
                updateSql,
                date,
                time.id().getValueAsUuid(),
                reservation.getId().getValueAsUuid()
        );

        return reservation.updateDateAndTime(date, time);
    }

    public Reservation updateCanceled(
            Reservation reservation,
            boolean canceled
    ) {
        String updateSql = "UPDATE reservation"
                + " SET canceled = ?"
                + " WHERE id = ?";

        jdbcTemplate.update(
                updateSql,
                canceled,
                reservation.getId().getValueAsUuid()
        );

        return reservation.updateCanceled(canceled);
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
        return (resultSet, rowNum) -> {
            EntityId timeId = readEntityId(resultSet, "time_id");
            LocalTime startAt = resultSet.getObject("start_at", LocalTime.class);
            ReservationTime time = new ReservationTime(timeId, startAt);

            return Reservation.retrieve(
                    readEntityId(resultSet, "id"),
                    resultSet.getString("name"),
                    resultSet.getObject("date", LocalDate.class),
                    resultSet.getBoolean("canceled"),
                    time,
                    readEntityId(resultSet, "theme_id")
            );
        };
    }

    private EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        UUID uuid = resultSet.getObject(column, UUID.class);

        return EntityId.fromUuid(uuid);
    }
}
