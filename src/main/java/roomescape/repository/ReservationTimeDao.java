package roomescape.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.repository.rowmapper.ReservationTimeRowMapper;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationTime> reservationTimeRowMapper;

    public ReservationTimeDao(final JdbcTemplate jdbcTemplate, final RowMapper<ReservationTime> reservationTimeRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationTimeRowMapper = reservationTimeRowMapper;
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "INSERT INTO reservation_time (start_at) VALUES (?)",
                            new String[]{"id"}
                    );
                    ps.setString(1, reservationTime.getStartAt().toString());
                    return ps;
                }, keyHolder
        );

        try {
            long id = keyHolder.getKey().longValue();
            return new ReservationTime(
                    id,
                    reservationTime.getStartAt()
            );
        } catch (NullPointerException exception) {
            throw new RuntimeException("[ERROR] 예약 시간 추가 요청이 정상적으로 이루어지지 않았습니다.");
        }
    }

    public Optional<ReservationTime> findById(final long id) {
        try {
            String sql = "SELECT * FROM reservation_time WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> getAll() {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                reservationTimeRowMapper
        );
    }

    public void delete(final long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", Long.valueOf(id));
    }
}
