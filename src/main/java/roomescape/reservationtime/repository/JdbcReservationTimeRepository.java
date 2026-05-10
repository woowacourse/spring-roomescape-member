package roomescape.reservationtime.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;

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
    public ReservationTime save(ReservationTime reservationTime) {
        String sql = """
                MERGE INTO reservation_time (start_at)
                KEY(start_at)
                VALUES (?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return ReservationTime.of(id, reservationTime.getStartAt());
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

}
