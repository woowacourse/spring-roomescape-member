package roomescape.reservation.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.entity.ReservationEntity;
import roomescape.time.domain.ReservationTime;

@Repository
@RequiredArgsConstructor
public class H2ReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getDate("date").toLocalDate(),
                    new ReservationTime(
                            rs.getLong("time_id"),
                            rs.getTime("start_at").toLocalTime()
                    )
            );
    ;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(ReservationEntity reservationEntity) {
        String sql = "INSERT INTO reservations (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        final int rowAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationEntity.name());
            ps.setDate(2, Date.valueOf(reservationEntity.date()));
            ps.setLong(3, reservationEntity.timeId());
            return ps;
        }, keyHolder);

        if (rowAffected != 1) {
            throw new IllegalStateException("예약 정보 저장에 실패했습니다.");
        }

        final Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("생성된 키가 존재하지 않습니다.");
        }

        return key.longValue();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.name AS name,
                    r.date AS date,
                    t.id AS time_id,
                    t.start_at AS start_at
                FROM reservations AS r
                INNER JOIN reservation_times AS t
                ON r.time_id = t.id
                WHERE r.id = ?
                """;
        final List<Reservation> reservations = jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, id);
        if (!reservations.isEmpty()) {
            return Optional.of(reservations.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public Long countByTimeId(Long timeId) {
        final String sql = """
                SELECT COUNT(*) AS count
                FROM reservations
                WHERE time_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, Long.class, timeId);
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                SELECT 
                    r.id AS id,
                    r.name AS name,
                    r.date AS date,
                    t.id AS time_id,
                    t.start_at AS start_at
                FROM reservations AS r
                INNER JOIN reservation_times AS t
                ON r.time_id = t.id
                """;

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    @Override
    public void deleteById(Long id) {
        final String sql = "DELETE FROM reservations WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
