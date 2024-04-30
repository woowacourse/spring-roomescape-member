package roomescape.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;

@Repository
public class JdbcTemplateReservationTimeRepository implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTimeRequest reservationTimeRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(reservationTimeRequest, keyHolder);
        return new ReservationTime(keyHolder.getKey().longValue(), reservationTimeRequest.startAt());
    }

    private void save(ReservationTimeRequest reservationTimeRequest, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement("insert into reservation_time(start_at) values ( ? )",
                    new String[]{"id"});
            pstmt.setTime(1, Time.valueOf(reservationTimeRequest.startAt()));
            return pstmt;
        }, keyHolder);
    }

    @Override
    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("select * from reservation_time", (rs, rowNum) -> {
            long id = rs.getLong(1);
            LocalTime time = rs.getTime(2).toLocalTime();
            return new ReservationTime(id, time);
        });
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }
}
