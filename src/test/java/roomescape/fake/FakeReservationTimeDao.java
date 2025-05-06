package roomescape.fake;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReservationTimeDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, ReservationTime> TIMES = new HashMap<>();

    public ReservationTime findById(Long id) {
        return TIMES.get(id);
    }

    public ReservationTime save(ReservationTime time) {
        Long id = IDX.getAndIncrement();
        ReservationTime newTime = new ReservationTime(id, time.getStartAt());
        TIMES.put(id, newTime);
        return newTime;
    }

    public List<ReservationTime> findAll() {
        return TIMES.values().stream().toList();
    }

    public int deleteById(Long id) {
        if (TIMES.containsKey(id)) {
            TIMES.remove(id);
            return 1;
        }
        return 0;
    }

    public boolean existByTimeValue(final LocalTime localTime) {
        long count = TIMES.values().stream()
                .filter(time -> time.getStartAt().equals(localTime))
                .count();
        return count != 0;
    }

    public void clear() {
        TIMES.clear();
        IDX.set(0);
    }
}
