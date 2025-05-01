package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTime;

@Repository
@RequiredArgsConstructor
public class H2ReservationTimeRepository implements ReservationTimeRepository {

    private static final RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER = (rs, rowNum) ->
            new ReservationTime(rs.getLong("id"),
                    rs.getTime("start_at").toLocalTime());

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(final ReservationTime reservationTime) {
        final String sql = "INSERT INTO reservation_times (start_at) VALUES (?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        final int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        if (rowAffected != 1) {
            throw new IllegalStateException("예약 시간 저장에 실패했습니다.");
        }

        final Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("생성된 키가 존재하지 않습니다.");
        }

        return key.longValue();
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "DELETE FROM reservation_times WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<ReservationTime> findById(final Long id) {
        final String sql = """
                SELECT
                    id,
                    start_at
                FROM reservation_times 
                WHERE id = ?
                """;
        final List<ReservationTime> reservationTimes = jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER, id);

        if (!reservationTimes.isEmpty()) {
            return Optional.of(reservationTimes.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public List<ReservationTime> findAllByStartAt(final LocalTime startAt) {
        final String sql = """
                SELECT
                    id,
                    start_at
                FROM reservation_times 
                WHERE start_at = ?
                """;

        return jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER, startAt);
    }

    @Override
    public List<ReservationTime> findAll() {
        final String sql = """
                SELECT
                    id,
                    start_at
                FROM reservation_times
                """;
        return jdbcTemplate.query(sql, RESERVATION_TIME_ROW_MAPPER);
    }
}
