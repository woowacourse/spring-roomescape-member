package roomescape.infrastructure;

import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.repository.TimeRepository;

@Repository
public class JdbcTimeRepository implements TimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static RowMapper<ReservationTime> RESERVATION_TIME_ROW_MAPPER =
            (rs, rowNum) -> ReservationTime.of(
                    rs.getLong("id"),
                    rs.getTime("start_at").toLocalTime()
            );

    @Override
    public Long save(ReservationTime reservationTime) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> params = Map.of(
                "start_at", Time.valueOf(reservationTime.getStartAt())
        );

        Number key = simpleJdbcInsert.executeAndReturnKey(params);
        return key.longValue();
    }

    @Override
    public List<ReservationTime> findAll() {
        String findAllSql = "SELECT id, start_at FROM reservation_time";

        return jdbcTemplate.query(findAllSql, RESERVATION_TIME_ROW_MAPPER);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        String selectOneSql = "SELECT id, start_at FROM reservation_time WHERE id=?";
        try {
            ReservationTime time = jdbcTemplate.queryForObject(selectOneSql, RESERVATION_TIME_ROW_MAPPER,
                    id);
            return Optional.of(time);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String deleteSql = "DELETE FROM reservation_time WHERE id=?";
        try {
            jdbcTemplate.update(deleteSql, id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }
}
