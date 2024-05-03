package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) -> new ReservationTime(
            rs.getLong("id"),
            rs.getTime("start_at").toLocalTime());

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation_time", rowMapper);
    }

    public ReservationTime findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?",
                rowMapper, id);
    }

    public boolean existByStartAt(LocalTime startAt) {
        return jdbcTemplate.queryForObject(
                "SELECT EXISTS(SELECT * FROM reservation_time WHERE start_at = ?)",
                Boolean.class, startAt);
    }

    public ReservationTime save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource("start_at",
                reservationTime.getStartAt());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return reservationTime.withId(id);
    }

    public boolean deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id) > 0;
    }
}
