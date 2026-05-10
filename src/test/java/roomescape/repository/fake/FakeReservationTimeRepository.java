package roomescape.repository.fake;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> reservationTimes = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime saved = new ReservationTime(
                counter.getAndIncrement(),
                reservationTime.getStartAt()
        );
        reservationTimes.add(saved);
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.removeIf(time -> time.getId().equals(id));
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsByStartAt(LocalTime time) {
        return reservationTimes.stream()
                .anyMatch(reservationTime
                        -> reservationTime.getStartAt().equals(time));
    }

    @Override
    public List<ReservationTime> findAllByPaging(int page, int size) {
        int offset = page * size;

        return reservationTimes.stream()
                .sorted(Comparator.comparing(ReservationTime::getId).reversed())
                .skip(offset)
                .limit(size)
                .toList();
    }

    @Override
    public List<ReservationTime> findTimeSlotsForReservationStatus() {
        return reservationTimes.stream()
                .sorted(Comparator.comparing(ReservationTime::getStartAt).thenComparing(ReservationTime::getId))
                .toList();
    }
}
