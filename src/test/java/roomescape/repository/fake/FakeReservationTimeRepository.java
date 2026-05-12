package roomescape.repository.fake;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> store = new HashMap<>();
    private long nextId = 1L;

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<ReservationTime> findAll(int limit, int offset) {
        return store.values().stream()
                .sorted(Comparator.comparing(ReservationTime::getId))
                .skip(offset)
                .limit(limit)
                .toList();
    }

    @Override
    public ReservationTime findById(Long id) {
        return store.get(id);
    }

    @Override
    public Long save(ReservationTime reservationTime) {
        Long id = nextId++;
        store.put(id, reservationTime.withId(id));
        return id;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}