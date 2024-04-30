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
import roomescape.infrastructure.rowmapper.ReservationRowMapper;

@Repository
public class JdbcReservationRepository implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingColumns("name", "date", "time_id")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = """
                select reservation.id as id, name, date, time_id, start_at \s
                from reservation left join reservation_time \s
                on time_id = reservation_time.id \s
                where reservation.id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, ReservationRowMapper.getInstance(), id);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select reservation.id as id, name, date, time_id, start_at \s
                from reservation left join reservation_time \s
                on time_id = reservation_time.id
                """;
        return jdbcTemplate.query(sql, ReservationRowMapper.getInstance());
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", time.getId());
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
    public boolean existByNameAndDateAndTimeId(String name, LocalDate date, long timeId) {
        String sql = "select count(*) from reservation where name = ? and date = ? and time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name, date, timeId);
        return count != null && count > 0;
    }
}
