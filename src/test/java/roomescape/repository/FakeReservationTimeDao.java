package roomescape.repository;

import roomescape.domain.ReservationTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeReservationTimeDao implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public ReservationTime findById(long id) {
        return storage.get(id);
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        long id = sequence++;
        ReservationTime savedTime = new ReservationTime(id, reservationTime.startAt());
        storage.put(id, savedTime);
        return savedTime;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }
}
