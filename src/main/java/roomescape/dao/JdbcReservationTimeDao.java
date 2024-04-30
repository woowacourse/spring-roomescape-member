package roomescape.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("SELECT * FROM reservation_time",
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id"),
                        rs.getTime("start_at")
                ));
    }

    @Override
    public ReservationTime findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM reservation_time WHERE id = ?",
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id"),
                        rs.getTime("start_at")
                ), id);
    }

    @Override
    public long save(ReservationTime reservationTime) {
        SqlParameterSource params = new MapSqlParameterSource("start_at", reservationTime.getStartAt());

        return simpleJdbcInsert.executeAndReturnKey(params)
                .longValue();
    }

    @Override
    public boolean deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM reservation_time WHERE id = ?", id) > 0;
    }
}
