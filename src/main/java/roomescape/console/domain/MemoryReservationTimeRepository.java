package roomescape.console.domain;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

public class MemoryReservationTimeRepository implements ReservationTimeRepository {
    private final Map<Long, ReservationTime> times;
    private final AtomicLong index;

    public MemoryReservationTimeRepository() {
        this.times = new HashMap<>();
        this.index = new AtomicLong(1L);
    }

    @Override
    public List<ReservationTime> findAll() {
        return times.values().stream()
                .sorted(Comparator.comparing(ReservationTime::getId))
                .toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(times.get(id));
    }

    @Override
    public ReservationTime create(ReservationTime reservationTime) {
        ReservationTime createdReservationTime = new ReservationTime(
                index.getAndIncrement(),
                reservationTime.getStartAt()
        );
        times.put(createdReservationTime.getId(), createdReservationTime);
        return createdReservationTime;
    }

    @Override
    public void removeById(Long id) {
        times.remove(id);
    }

    @Override
    public boolean hasDuplicateTime(ReservationTime reservationTime) {
        return false;
    }
}
