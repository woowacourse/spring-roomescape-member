package roomescape.time.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) ->
            new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        List<ReservationTime> results = jdbcTemplate.query(sql, reservationTimeRowMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("select id, start_at from reservation_time", reservationTimeRowMapper);
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "select exists (select 1 from reservation_time where start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, startAt);
    }

    @Override
    public List<AvailableTimeQueryResult> findAvailableTimes(Long themeId, LocalDate date) {
        String sql = "SELECT rt.id, rt.start_at\n" +
                "FROM reservation_time rt\n" +
                "LEFT JOIN reservation r\n" +
                "  ON rt.id = r.time_id\n" +
                "  AND r.theme_id = ?\n" +
                "  AND r.reservation_date = ?\n" +
                "WHERE r.id IS NULL";

        RowMapper<AvailableTimeQueryResult> reservationTimeMapper = (rs, rowNum) ->
                new AvailableTimeQueryResult(
                        rs.getLong("id"),
                        rs.getTime("start_at").toLocalTime()
                );
        return jdbcTemplate.query(sql, reservationTimeMapper, themeId, date);
    }
}
