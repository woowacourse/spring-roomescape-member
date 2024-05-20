package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime
                = new ReservationTime(
                resultSet.getLong("id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );
        return reservationTime;
    };

    public ReservationTimeJdbcRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public ReservationTime findByTimeId(Long timeId) {
        try {
            String sql = "SELECT id, start_at FROM reservation_time WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, timeId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간입니다.");
        }
    }

    public ReservationTime save(ReservationTime reservationTime) {
        try {
            SqlParameterSource parameterSource = new MapSqlParameterSource()
                    .addValue("start_at", reservationTime.getStartAt());
            Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
            return new ReservationTime(id, reservationTime.getStartAt());
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 추가된 예약 시간입니다.");
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("현 예약 시간에 예약이 존재합니다.");
        }
    }
}
