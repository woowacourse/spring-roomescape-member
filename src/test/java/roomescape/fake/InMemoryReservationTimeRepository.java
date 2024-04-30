package roomescape.fake;

import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReservationTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> reservationTimes = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong index = new AtomicLong(0);

    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime newReservationTime = new ReservationTime(
                index.incrementAndGet(),
                reservationTime.getStartAt());
        reservationTimes.add(newReservationTime);

        return newReservationTime;
    }

    public List<ReservationTime> findAll() {
        return Collections.unmodifiableList(reservationTimes);
    }

    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.hasId(id))
                .findFirst();
    }

    public void deleteById(long id) {
        reservationTimes.stream()
                .filter(reservationTime -> reservationTime.hasId(id))
                .findFirst()
                .ifPresent(reservationTimes::remove);
    }
}
