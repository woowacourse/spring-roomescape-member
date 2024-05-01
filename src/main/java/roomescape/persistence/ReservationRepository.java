package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationRepository {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                """
                        SELECT 
                            r.id AS reservation_id, 
                            r.name, 
                            r.date, 
                            t.id AS time_id, 
                            t.start_at AS time_value,
                            th.id AS theme_id,
                            th.name AS theme_name,
                            th.description,
                            th.thumbnail
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                        INNER JOIN theme AS th
                        ON r.theme_id = th.id""",
                reservationRowMapper());
    }

    public Reservation findById(Long id) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT 
                            r.id AS reservation_id, 
                            r.name, 
                            r.date, 
                            t.id AS time_id, 
                            t.start_at AS time_value,
                            th.id AS theme_id,
                            th.name AS theme_name,
                            th.description,
                            th.thumbnail
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                        INNER JOIN theme AS th
                        ON r.theme_id = th.id
                        WHERE r.id = ?""",
                reservationRowMapper(), id);
    }

    public Reservation create(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName().value())
                .addValue("date", reservation.getDate().format(DATE_FORMATTER))
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public void removeById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    public boolean hasDuplicateReservation(Reservation reservation) {

        return jdbcTemplate.queryForObject(
                """
                        SELECT count(*)
                        FROM reservation
                        WHERE date = ? AND time_id = ? AND theme_id = ?""",
                Integer.class, reservation.getDate(), reservation.getTime().getId(), reservation.getTheme().getId())
                > 0;
    }

    public boolean hasByTimeId(Long id) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT count(*)
                        FROM reservation
                        WHERE time_id = ?""",
                Integer.class, id)
                > 0;
    }

    public boolean hasByThemeId(Long id) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT count(*)
                        FROM reservation
                        WHERE theme_id = ?""",
                Integer.class, id)
                > 0;
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> {
            LocalTime startAt = LocalTime.parse(resultSet.getString("start_at"));
            Reservation reservation = new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Name(resultSet.getString("name")),
                    LocalDate.parse(resultSet.getString("date")),
                    new ReservationTime(resultSet.getLong("time_id"), startAt),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail")
                    )
            );
            return reservation;
        };
    }
}
