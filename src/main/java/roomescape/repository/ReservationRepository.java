package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

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
                "id", reservation.getId().toString(),
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getTimeId().toString(),
                "theme_id", reservation.getThemeId().toString()
        ));

        return reservation;
    }

    public List<Reservation> findAll() {
        String findSql = "SELECT r.id AS reservation_id,"
                + " r.name AS member_name,"
                + " r.date AS reservation_date,"
                + " r.time_id,"
                + " r.theme_id,"
                + " rt.start_at AS time_start_at,"
                + " t.name AS theme_name,"
                + " t.description AS theme_description,"
                + " t.image_url AS theme_image_url"
                + " FROM reservation r"
                + " INNER JOIN reservation_time rt ON r.time_id = rt.id"
                + " INNER JOIN theme t ON r.theme_id = t.id";

        return jdbcTemplate.query(findSql, reservationRowMapper());
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
        return (resultSet, rowNum) -> {
            UUID timeId = UUID.fromString(resultSet.getString("time_id"));
            LocalTime startAt = resultSet.getObject("time_start_at", LocalTime.class);

            UUID themeId = UUID.fromString(resultSet.getString("theme_id"));
            String themeName = resultSet.getString("theme_name");
            String description = resultSet.getString("theme_description");
            String imageUrl = resultSet.getString("theme_image_url");

            return new Reservation(
                    UUID.fromString(resultSet.getString("reservation_id")),
                    resultSet.getString("member_name"),
                    resultSet.getObject("reservation_date", LocalDate.class),
                    new ReservationTime(timeId, startAt),
                    new Theme(themeId, themeName, description, imageUrl)
            );
        };
    }
}
