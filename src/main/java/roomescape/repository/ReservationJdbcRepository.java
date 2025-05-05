package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Reservation> findById(final long id) {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at, R.theme_id, T.name as theme_name, T.description as theme_description, T.thumbnail as theme_thumbnail from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id 
            left join THEME T on T.id = R.theme_id                                                                                                                              
            where R.id = ?
            """;

        var reservationList = jdbcTemplate.query(sql, RowMappers.RESERVATION, id);
        return reservationList.stream().findAny();
    }

    @Override
    public long save(final Reservation reservation) {
        var insert = new SimpleJdbcInsert(jdbcTemplate);
        var generatedId = insert.withTableName("RESERVATION")
            .usingGeneratedKeyColumns("id")
            .executeAndReturnKey(Map.of(
                "name", reservation.name(),
                "date", reservation.date(),
                "time_id", reservation.timeSlot().id(),
                "theme_id", reservation.theme().id()
            ));
        return generatedId.longValue();
    }

    @Override
    public boolean removeById(final long id) {
        var sql = "delete from RESERVATION where id = ?";

        var removedRowsCount = jdbcTemplate.update(sql, id);
        return removedRowsCount > 0;
    }

    @Override
    public List<Reservation> findAll() {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at, R.theme_id, T.name as theme_name, T.description as theme_description, T.thumbnail as theme_thumbnail from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id 
            left join THEME T on T.id = R.theme_id
            """;

        return jdbcTemplate.query(sql, RowMappers.RESERVATION);
    }

    @Override
    public List<Reservation> findByTimeSlotId(final long id) {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at, R.theme_id, T.name as theme_name, T.description as theme_description, T.thumbnail as theme_thumbnail from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id
            left join THEME T on T.id = R.theme_id
            WHERE R.time_id = ?
            """;

        return jdbcTemplate.query(sql, RowMappers.RESERVATION, id);
    }

    @Override
    public List<Reservation> findByThemeId(final long id) {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at, R.theme_id, T.name as theme_name, T.description as theme_description, T.thumbnail as theme_thumbnail from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id 
            left join THEME T on T.id = R.theme_id
            WHERE R.theme_id = ?
            """;

        return jdbcTemplate.query(sql, RowMappers.RESERVATION, id);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final long themeId) {
        var sql = """
            select R.id, R.name, R.date, R.time_id, RT.start_at, R.theme_id, T.name as theme_name, T.description as theme_description, T.thumbnail as theme_thumbnail from RESERVATION R
            left join RESERVATION_TIME RT on R.time_id = RT.id 
            left join THEME T on T.id = R.theme_id
            WHERE R.date = ? AND R.theme_id = ?
            """;

        return jdbcTemplate.query(sql, RowMappers.RESERVATION, date, themeId);
    }
}
