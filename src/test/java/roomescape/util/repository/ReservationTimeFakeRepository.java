package roomescape.util.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

public class ReservationTimeFakeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> times = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Long saveAndReturnId(ReservationTime time) {
        Long id = idGenerator.incrementAndGet();
        times.put(id, time.withId(id));
        return id;
    }

    @Override
    public List<ReservationTime> findAll() {
        return times.values().stream().toList();
    }

    @Override
    public int deleteById(Long id) {
        if (times.containsKey(id)) {
            times.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public ReservationTime findById(Long id) {
        return times.get(id);
    }

    @Override
    public Boolean existSameStartAt(LocalTime time) {
        return times.values().stream()
                .anyMatch(reservationTime -> reservationTime.getStartAt().equals(time));
    }
}
