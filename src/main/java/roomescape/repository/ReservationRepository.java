package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Duration;
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
                "id", reservation.id().toString(),
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", reservation.timeId().toString(),
                "theme_id", reservation.themeId().toString()
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

    public List<Reservation> findByDateAndThemeId(LocalDate date, UUID themeId) {
        String findSql = "SELECT id, name, date, time_id, theme_id"
                + " FROM reservation r"
                + " WHERE date = ? AND theme_id = ?";

        return jdbcTemplate.query(
                findSql,
                reservationRowMapper(),
                date,
                themeId
        );
    }

    public boolean delete(UUID reservationId) {
        String deleteSql = "DELETE FROM reservation"
                + " WHERE id = ?";

        int deletedRowCount = jdbcTemplate.update(deleteSql, reservationId.toString());

        return isDeleted(deletedRowCount);
    }

    private boolean isDeleted(int deletedRowCount) {
        return deletedRowCount > 0;
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                UUID.fromString(resultSet.getString("time_id")),
                UUID.fromString(resultSet.getString("theme_id"))
        );
    }
}
