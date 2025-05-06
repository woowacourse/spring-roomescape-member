package roomescape.repository;

import static java.time.LocalTime.parse;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.model.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime addTime(ReservationTime reservationTime) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation_time (start_at) values (?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationTime.getStartAt().toString());
            return ps;
        }, keyHolder);
        return new ReservationTime(Objects.requireNonNull(keyHolder.getKey())
                .longValue(), reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> getAllTime() {
        String sql = "select * from reservation_time";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new ReservationTime(
                rs.getLong("id"),
                parse(rs.getString("start_at"))
        ));
    }

    @Override
    public int deleteTime(Long id) {
        return jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String sql = "SELECT start_at FROM reservation_time WHERE id = ?";

        List<String> results = jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getString("start_at"), id);

        return results.stream()
                .findFirst()
                .map(startAt -> new ReservationTime(id, parse(startAt)));
    }

}
