package roomescape.repository.reservationtime;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.entity.ReservationTimeEntity;

@Repository
public class ReservationTimeJdbcRepository implements ReservationTimeRepository {
    private static final RowMapper<ReservationTimeEntity> ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTimeEntity(
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
    public ReservationTimeEntity add(ReservationTime time) {
        Map<String, LocalTime> params = new HashMap<>();
        params.put("start_at", time.getStartAt());

        Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return ReservationTimeEntity.of(id, time);
    }

    @Override
    public List<ReservationTimeEntity> findAll() {
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
    public Optional<ReservationTimeEntity> findById(Long timeId) {
        String sql = "select * from reservation_time where id = ?";
        List<ReservationTimeEntity> time = jdbcTemplate.query(
                sql,
                ROW_MAPPER,
                timeId
        );
        return time.stream().findFirst();
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        String sql = "select COUNT(*) from reservation_time where start_at = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, startAt);

        return count != 0;
    }
}
