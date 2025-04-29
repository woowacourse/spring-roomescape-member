package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationTimeDao;
import roomescape.model.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final Map<Long, ReservationTime> reservationTimes = new ConcurrentHashMap<>();
    private final AtomicLong id = new AtomicLong(1L);

    @Override
    public ReservationTime save(ReservationTime time) {
        Long id = this.id.getAndIncrement();
        ReservationTime savedTime = new ReservationTime(id, time.getStartAt());
        reservationTimes.put(id, savedTime);

        return savedTime;
    }

    @Override
    public boolean deleteById(Long id) {
        return reservationTimes.remove(id) != null;
    }

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(reservationTimes.values());
    }

    @Override
    public ReservationTime findById(Long id) {
        return reservationTimes.get(id);
    }
}
