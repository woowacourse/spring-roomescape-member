/*
package roomescape.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {
    private final Map<Long, ReservationTime> fakeReservationTimeDB = new HashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        long idx = index.getAndIncrement();
        fakeReservationTimeDB.put(idx, new ReservationTime(idx, reservationTime.getStartAt()));
        return new ReservationTime(idx, reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        return fakeReservationTimeDB.values().stream().toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(fakeReservationTimeDB.get(id));
    }

    @Override
    public void deleteById(Long id) {
        fakeReservationTimeDB.remove(id);
    }

    public void deleteAll() {
        fakeReservationTimeDB.clear();
    }
}
*/
