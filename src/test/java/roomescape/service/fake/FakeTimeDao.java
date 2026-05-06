package roomescape.service.fake;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;

public class FakeTimeDao implements TimeDao {

    private final Map<Long, Time> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public List<Time> findAll() {
        return store.values().stream().toList();
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
    public Time insert(Time time) {
        Long id = ++sequence;
        Time newTime = new Time(id, time.getStartAt());
        store.put(id, newTime);
        return newTime;
    }

    @Override
    public int delete(Long id) {
        Time remove = store.remove(id);

        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return store.values()
                .stream()
                .anyMatch(theme -> theme.getStartAt().equals(startAt));
    }
}
