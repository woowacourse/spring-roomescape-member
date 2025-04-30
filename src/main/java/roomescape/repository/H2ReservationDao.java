package roomescape.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;
import roomescape.service.reservation.Theme;

@Repository
public class H2ReservationDao implements ReservationDao {

    public static final String RESERVATION_TABLE = "reservation";
    public static final String RESERVATION_PK = "id";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert reservationInserter;
    private final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getTime("time_value").toLocalTime()
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    )
            );

    public H2ReservationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationInserter = new SimpleJdbcInsert(dataSource)
                .withTableName(RESERVATION_TABLE)
                .usingGeneratedKeyColumns(RESERVATION_PK);
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        long id = insertReservationAndRetrieveKey(reservation);
        return getReservationById(id);
    }

    @Override
    public void deleteById(final long id) {
        final String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean isExistsByDateAndTimeId(final LocalDate date, final long timeId) {
        final String sql = "SELECT count(*) FROM reservation WHERE date = ? AND time_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, date, timeId);
        return count > 0;
    }

    @Override
    public boolean isExistsByTimeId(final long timeId) {
        final String sql = "SELECT count(*) FROM reservation WHERE time_id = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, timeId);
        return count > 0;
    }

    private Reservation getReservationById(final long id) {
        final String sql = """
                SELECT
                    r.id AS reservation_id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    th.id AS theme_id,
                    th.name AS theme_name,
                    th.description AS theme_description,
                    th.thumbnail AS theme_thumbnail
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, reservationRowMapper, id);
    }

    private long insertReservationAndRetrieveKey(final Reservation reservation) {
        final Map<String, Object> parameters = Map.of(
                "name", reservation.getName(),
                "date", Date.valueOf(reservation.getDate()),
                "time_id", reservation.getTimeId(),
                "theme_id", reservation.getTheme().getId()
        );
        return reservationInserter.executeAndReturnKey(parameters).longValue();
    }
}
