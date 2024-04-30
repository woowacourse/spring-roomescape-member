package roomescape.time.repository;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.domain.ReservationTime;

@Repository
public class ReservationTimeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long save(ReservationTime reservationTime) {
        String sql = "insert into reservation_time (start_at) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql, new String[]{"id"}
            );
            ps.setString(1, String.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public ReservationTime findById(Long id) {
        String sql = "select id, start_at from reservation_time where id = ?";
        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> {
                    return new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    );
                }, id);
    }

    public List<ReservationTime> findAll() {
        String sql = "select id, start_at from reservation_time";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {
                    return new ReservationTime(
                            rs.getLong("id"),
                            rs.getTime("start_at").toLocalTime()
                    );
                });
    }

    public void delete(Long id) {
        String sql = "delete from reservation_time where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
