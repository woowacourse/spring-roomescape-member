package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {
    private static final RowMapper<ReservationTime> ROW_MAPPER = (resultSet, rowNum) ->
            ReservationTime.createWithId(
                    resultSet.getLong("id"),
                    resultSet.getTime("start_at").toLocalTime()
            );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationTimeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime add(ReservationTime time) {
        Map<String, LocalTime> params = new HashMap<>();
        params.put("start_at", time.getStartAt());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationTime.createWithId(id, time.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        String sql = "select * from reservation_time";
        return jdbcTemplate.query(
                sql,
                ROW_MAPPER
        );
    }

    @Override
    public int deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long timeId) {
        String sql = "select * from reservation_time where id = ?";
        List<ReservationTime> time = jdbcTemplate.query(
                sql,
                ROW_MAPPER,
                timeId
        );
        return time.stream().findFirst();
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "select exists(select 1 from reservation_time where start_at = ?)";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, startAt));
    }
}
