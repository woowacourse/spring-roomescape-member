package roomescape.reservationtime.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> db = new ArrayList<>();
    AtomicLong count = new AtomicLong();

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime saveReservation = new ReservationTime(
                count.incrementAndGet(),
                reservationTime.getTime()
        );
        db.add(saveReservation);
        return saveReservation;
    }

    @Override
    public List<ReservationTime> findAll() {
        return db;
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return db.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findAny();
    }

    @Override
    public void deleteById(Long id) {
        findById(id).ifPresent(db::remove);
    }

    public void clear() {
        db.clear();
        count.set(0);
    }
}
