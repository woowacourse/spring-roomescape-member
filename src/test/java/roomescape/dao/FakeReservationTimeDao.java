package roomescape.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.model.ReservationTime;

public class FakeReservationTimeDao extends ReservationTimeDao {
    public FakeReservationTimeDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    private final List<ReservationTime> times = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1L);

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(times);
    }

    @Override
    public Long saveTime(ReservationTime reservationTime) {
        ReservationTime newTime = new ReservationTime(nextId.getAndIncrement(), reservationTime.getStartAt());
        times.add(newTime);
        return newTime.getId();
    }

    @Override
    public void deleteTimeById(Long id) {
        times.removeIf(time -> time.getId().equals(id));
    }

}
