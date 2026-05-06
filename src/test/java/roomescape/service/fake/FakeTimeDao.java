package roomescape.service.fake;

import roomescape.dao.TimeDao;
import roomescape.domain.Time;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeTimeDao implements TimeDao {

    private final Map<Long, Time> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Time insert(Time time) {
        Long id = ++sequence;
        Time newTime = new Time(id, time.getStartAt());
        store.put(id, newTime);
        return newTime;
    }

    @Override
    public Optional<Time> findById(Long id) {
        Time time = store.get(id);

        if (time == null) {
            return Optional.empty();
        }

        return Optional.of(time);
    }

    @Override
    public List<Time> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public int delete(Long id) {
        Time remove = store.remove(id);

        if (remove == null) {
            return 0;
        }
        return 1;
    }
}
