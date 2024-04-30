package roomescape.domain;

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

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
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
                            t.start_at AS time_value 
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t 
                        ON r.time_id = t.id""",
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
                            t.start_at AS time_value
                        FROM reservation AS r 
                        INNER JOIN reservation_time AS t
                        ON r.time_id = t.id
                        WHERE r.id = ?""",
                reservationRowMapper(), id);
    }

    public Reservation create(Reservation reservation) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName().getValue())
                .addValue("date", reservation.getDate().format(DATE_FORMATTER))
                .addValue("time_id", reservation.getTime().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime());
    }

    public void removeById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> {
            LocalTime startAt = LocalTime.parse(resultSet.getString("start_at"));
            Reservation reservation = new Reservation(
                    resultSet.getLong("reservation_id"),
                    new Name(resultSet.getString("name")),
                    LocalDate.parse(resultSet.getString("date")),
                    new ReservationTime(resultSet.getLong("time_id"), startAt)
            );
            return reservation;
        };
    }
}
