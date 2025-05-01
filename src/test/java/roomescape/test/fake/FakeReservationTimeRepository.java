package roomescape.test.fake;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository extends ReservationTimeRepository {

    private final Map<Long, ReservationTime> reservationTimes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    public FakeReservationTimeRepository() {
        super(null);
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.values().stream().toList();
    }

    @Override
    public Optional<ReservationTime> findById(long id) {
        if (!reservationTimes.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(reservationTimes.get(id));
    }

    @Override
    public boolean findByStartAt(LocalTime startAt) {
        return reservationTimes.values().stream()
                .anyMatch(time -> time.getStartAt().equals(startAt));
    }

    @Override
    public long add(ReservationTime reservationTime) {
        ReservationTime newReservationTime = new ReservationTime(index.getAndIncrement(), reservationTime.getStartAt());
        reservationTimes.put(newReservationTime.getId(), newReservationTime);
        return newReservationTime.getId();
    }

    @Override
    public void deleteById(long id) {
        reservationTimes.remove(id);
    }
}
