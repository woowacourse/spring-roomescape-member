package roomescape.reservation.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.exception.ReservationTimeNotFoundException;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> reservationTimeRowMapper = (rs, rowNum) ->
            ReservationTime.of(
                    rs.getLong("id"),
                    rs.getObject("start_at", LocalTime.class)
            );

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(LocalTime startAt) {
        String sql = """
                MERGE INTO reservation_time (start_at)
                KEY(start_at)
                VALUES (?)
                """;

        jdbcTemplate.update(sql, startAt);
        return findIdByStartAt(startAt);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
        List<ReservationTime> result = jdbcTemplate.query(sql, reservationTimeRowMapper, id);
        return result.stream().findFirst();
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time ORDER BY id";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";

        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            throw new ReservationTimeNotFoundException(id);
        }
    }

    private Long findIdByStartAt(LocalTime startAt) {
        String sql = "SELECT id FROM reservation_time WHERE start_at = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, startAt);
    }

}
