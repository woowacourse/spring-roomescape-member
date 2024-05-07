package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.dao.condition.TimeInsertCondition;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public ReservationTime insert(TimeInsertCondition insertCondition) {
        LocalTime time = insertCondition.getStartAt();
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("start_at", time.toString());

        Long id = insertActor.executeAndReturnKey(parameters).longValue();

        return new ReservationTime(id, time);
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time";
        return jdbcTemplate.query(sql, getReservationTimeRowMapper());
    }

    public Optional<ReservationTime> findById(Long id) {
        String sql = """
                SELECT 
                    id, 
                    start_at 
                FROM reservation_time 
                WHERE id = ?
                """;
        List<ReservationTime> reservationTimes = jdbcTemplate.query(sql, getReservationTimeRowMapper(), id);

        return Optional.ofNullable(DataAccessUtils.singleResult(reservationTimes));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean hasSameTime(LocalTime time) {
        String sql = "SELECT EXISTS(SELECT start_at FROM reservation_time WHERE start_at = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, time.toString());
    }

    private RowMapper<ReservationTime> getReservationTimeRowMapper() {
        return (resultSet, numRow) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }
}
