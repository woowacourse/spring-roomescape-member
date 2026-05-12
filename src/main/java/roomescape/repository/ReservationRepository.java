package roomescape.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
        simpleJdbcInsert.execute(Map.of(
                "id", reservation.id().toBytes(),
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", reservation.timeId().toBytes(),
                "theme_id", reservation.themeId().toBytes()
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
                themeId.toBytes()
        );
    }

    public boolean delete(EntityId reservationId) {
        String deleteSql = "DELETE FROM reservation"
                + " WHERE id = ?";

        int deletedRowCount = jdbcTemplate.update(deleteSql, reservationId.toBytes());

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

    private static EntityId readEntityId(ResultSet resultSet, String column) throws SQLException {
        return EntityId.fromBytes(resultSet.getBytes(column));
    }
}
