package roomescape.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.ReservationTimeRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2ReservationTimeRepository implements ReservationTimeRepository {
    private final ReservationTimeRowMapper rowMapper;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public H2ReservationTimeRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.rowMapper = new ReservationTimeRowMapper();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime save(ReservationTime reservationTime) {
        Long reservationTimeId = jdbcInsert.executeAndReturnKey(Map.of(
                        "start_at", reservationTime.getStartAt()))
                .longValue();

        return new ReservationTime(
                reservationTimeId,
                reservationTime.getStartAt());
    }

    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = "select * from reservation_time where id = ?";
        try {
            ReservationTime savedReservationTime = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(savedReservationTime);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("해당 시간은 예약이 존재하여 삭제할 수 없습니다. ");
        }
    }

    private static class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
        @Override
        public ReservationTime mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            return new ReservationTime(
                    rs.getLong("id"),
                    rs.getTime("start_at").toLocalTime());
        }
    }
}
