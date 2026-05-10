package roomescape.repository.jdbc;

import static roomescape.repository.jdbc.ReservationTimeEntityMapper.RESERVATION_TIME_MAPPER;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

@Repository
@RequiredArgsConstructor
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO reservation_time (start_at) VALUES (?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        return new ReservationTime(keyHolder.getKey().longValue(), reservationTime.getStartAt());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        try {
            String sql = "SELECT * FROM reservation_time WHERE id = ?";
            ReservationTime time = jdbcTemplate.queryForObject(sql, RESERVATION_TIME_MAPPER, id);
            return Optional.ofNullable(time);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByStartAt(LocalTime time) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation_time WHERE start_at = ?)";
        Boolean result = jdbcTemplate.queryForObject(sql, Boolean.class, time);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public List<ReservationTime> findAllByPaging(int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM reservation_time ORDER BY start_at ASC, id ASC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, RESERVATION_TIME_MAPPER, size, offset);
    }

    @Override
    public List<ReservationTime> findTimeSlotsForReservationStatus() {
        String sql = """
                        SELECT *
                        FROM reservation_time
                        ORDER BY start_at ASC, id ASC
                """;

        return jdbcTemplate.query(sql, RESERVATION_TIME_MAPPER);
    }
}
