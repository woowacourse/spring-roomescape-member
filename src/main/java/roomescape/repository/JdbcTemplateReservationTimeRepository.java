package roomescape.repository;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimes;

@Repository
public class JdbcTemplateReservationTimeRepository implements ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateReservationTimeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        save(reservationTime, keyHolder);
        return new ReservationTime(keyHolder.getKey().longValue(), reservationTime.getStartAt());
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return jdbcTemplate.queryForObject(
                "select exists(select 1 from RESERVATION_TIME where START_AT = ?)",
                Boolean.class, startAt);
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        List<ReservationTime> times = jdbcTemplate.query("select start_at from reservation_time where id = ?",
                (rs, rowNum) -> {
                    LocalTime time = rs.getTime(1).toLocalTime();
                    return new ReservationTime(id, time);
                }, id);
        return times.stream().findFirst();
    }

    @Override
    public ReservationTimes findAll() {
        List<ReservationTime> findReservationTimes = jdbcTemplate.query("select * from reservation_time", (rs, rowNum) -> {
            long id = rs.getLong(1);
            LocalTime time = rs.getTime(2).toLocalTime();
            return new ReservationTime(id, time);
        });
        return new ReservationTimes(findReservationTimes);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }

    private void save(ReservationTime reservationTime, KeyHolder keyHolder) {
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement("insert into reservation_time(start_at) values ( ? )",
                    new String[]{"id"});
            pstmt.setTime(1, Time.valueOf(reservationTime.getStartAt()));
            return pstmt;
        }, keyHolder);
    }
}
