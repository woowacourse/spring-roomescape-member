package roomescape.domain.time.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import roomescape.domain.time.ReservationTime;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {
    private static final RowMapper<ReservationTime> ROW_MAPPER = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations jdbcInsert;

    public JdbcReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("start_at", reservationTime.getStartAt());
        long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new ReservationTime(id, reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        String query = "SELECT * FROM reservation_time WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        String query = "SELECT * FROM reservation_time";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    @Override
    public int deleteById(long id) {
        String query = "DELETE FROM reservation_time WHERE id = ?";
        return jdbcTemplate.update(query, id);
    }
}
