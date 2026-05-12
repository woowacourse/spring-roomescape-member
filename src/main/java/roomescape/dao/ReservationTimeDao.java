package roomescape.dao;

import static roomescape.dao.rowMapper.ReservationTimeMapper.RESERVATION_TIME_ROW_MAPPER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(
                "SELECT id, start_at FROM reservation_time",
                RESERVATION_TIME_ROW_MAPPER
        );
    }

    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("start_at", reservationTime.getStartAt());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new ReservationTime(id, reservationTime.getStartAt());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id);
    }

    public Optional<ReservationTime> findTimeById(Long timeId) {
        String sql = """
                SELECT id, start_at 
                FROM reservation_time 
                WHERE id = ?
                """;

        return jdbcTemplate.query(
                        sql,
                        RESERVATION_TIME_ROW_MAPPER,
                        timeId
                ).stream()
                .findFirst();
    }
}
