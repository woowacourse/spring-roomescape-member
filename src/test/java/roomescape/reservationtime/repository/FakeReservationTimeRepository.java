package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.reservationtime.domain.ReservationTime;

@Repository
public class FakeReservationTimeRepository implements ReservationTimeRepository {
    private final Map<Long, ReservationTime> reservationTimes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<ReservationTime> getAll() {
        return reservationTimes.entrySet().stream()
                .map(entry -> ReservationTime.of(entry.getKey(), entry.getValue().getStartAt()))
                .toList();
    }

    @Override
    public ReservationTime put(final ReservationTime reservationTime) {
        long id = index.getAndIncrement();
        reservationTimes.put(id, reservationTime);
        return ReservationTime.of(id, reservationTime.getStartAt());
    }

    @Override
    public boolean deleteById(final long id) {
        return reservationTimes.remove(id) != null;
    }

    @Override
    public Optional<ReservationTime> findById(final long id) {
        return Optional.ofNullable(reservationTimes.get(id));
    }

    @Override
    public boolean checkExistsByStartAt(final LocalTime time) {
        return reservationTimes.values().stream()
                .map(ReservationTime::getStartAt)
                .anyMatch(startAt -> startAt.equals(time));
    }
}
