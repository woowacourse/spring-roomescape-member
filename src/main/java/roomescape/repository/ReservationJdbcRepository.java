package roomescape.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.model.TimeSlot;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER =
        (rs, rowNum) -> {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            var date = rs.getDate("date").toLocalDate();
            var timeSlotId = rs.getLong("time_id");
            var time = rs.getTime("start_at").toLocalTime();
            return new Reservation(id, name, date, new TimeSlot(timeSlotId, time));
        };

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReservationJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Reservation> findById(final long id) {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id
            where R.id = ?
            """;

        var reservationList = jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, id);
        return reservationList.stream().findAny();
    }

    public long save(Reservation reservation) {
        var insert = new SimpleJdbcInsert(jdbcTemplate);
        var generatedId = insert.withTableName("RESERVATION")
            .usingGeneratedKeyColumns("id")
            .executeAndReturnKey(Map.of(
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", reservation.timeSlot().id()
            ));
        return generatedId.longValue();
    }

    public boolean removeById(long id) {
        var sql = "delete from RESERVATION where id = ?";

        var removedRowsCount = jdbcTemplate.update(sql, id);
        return removedRowsCount > 0;
    }

    public List<Reservation> findAll() {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id
            """;

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    public List<Reservation> findByTimeSlotId(long id) {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id
            WHERE R.time_id = ?
            """;

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, id);
    }
}
