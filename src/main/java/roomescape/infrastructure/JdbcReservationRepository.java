package roomescape.infrastructure;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.infrastructure.rowmapper.ReservationRowMapper;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingColumns("name", "date", "time_id", "theme_id")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = """
                select r.id as id, r.name as reservation_name, date, time_id, start_at,
                theme_id, t.name as theme_name, description, thumbnail from reservation as r
                left join reservation_time as rt on time_id = rt.id
                left join theme as t on theme_id = t.id
                where rt.id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, ReservationRowMapper::joinedMapRow, id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id as id, r.name as reservation_name, date, time_id, start_at,
                theme_id, t.name as theme_name, description, thumbnail from reservation as r
                left join reservation_time as rt on time_id = rt.id
                left join theme as t on theme_id = t.id
                """;
        return jdbcTemplate.query(sql, ReservationRowMapper::joinedMapRow);
    }

    @Override
    public Reservation create(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", time.getId())
                .addValue("theme_id", theme.getId());
        long id = jdbcInsert.executeAndReturnKey(parameters).longValue();
        return reservation.withId(id);
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public long findReservationCountByTimeId(long timeId) {
        String sql = "select count(*) from reservation where time_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, timeId);
    }

    @Override
    public boolean existByDateAndTimeId(LocalDate date, long timeId) {
        String sql = "select count(*) from reservation where date = ? and time_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, date, timeId) > 0;
    }
}
