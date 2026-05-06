package roomescape.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.domain.Time;

public class FakeReservationTimeDao implements ReservationTimeRepository {

    private final Map<Long, Time> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public List<Time> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Time findById(long id) {
        return storage.get(id);
    }

    @Override
    public Time save(Time time) {
        long id = sequence++;
        Time savedTime = new Time(id, time.startAt());
        storage.put(id, savedTime);
        return savedTime;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }
}
