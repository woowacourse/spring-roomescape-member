package roomescape.infrastructure;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static RowMapper<Reservation> RESERVATION_ROW_MAPPER =
            (rs, rowNum) -> {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                LocalDate date = rs.getObject("reservation_date", LocalDate.class);
                Long time_id = rs.getLong("time_id");
                LocalTime time = rs.getObject("time_value", LocalTime.class);

                return Reservation.of(id, name, date, ReservationTime.of(time_id, time));
            };

    @Override
    public List<Reservation> findAll() {
        String findAllSql = """
                SELECT
                    r.id as reservation_id,
                    r.name as name,
                    r.date as reservation_date,
                    t.id as time_id,
                    t.start_at as time_value
                FROM reservation as r
                join reservation_time as t
                on r.time_id = t.id
                """;
        return jdbcTemplate.query(findAllSql, RESERVATION_ROW_MAPPER);
    }

    @Override
    public Long save(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = Map.of(
                "name", reservation.getName(),
                "date", Date.valueOf(reservation.getReservationDate()),
                "time_id", reservation.getReservationTime().getId()
        );

        Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        return key.longValue();
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM reservation WHERE id=?";
        jdbcTemplate.update(deleteSql, id);
    }
}
