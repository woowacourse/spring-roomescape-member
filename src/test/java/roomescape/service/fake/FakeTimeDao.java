package roomescape.service.fake;

import roomescape.dao.TimeDao;
import roomescape.dao.row.TimeRow;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeTimeDao implements TimeDao {

    private final Map<Long, TimeRow> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public List<TimeRow> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public Optional<TimeRow> findById(Long id) {
        TimeRow time = store.get(id);

        if (time == null) {
            return Optional.empty();
        }

        return Optional.of(time);
    }

    @Override
    public TimeRow create(TimeRow time) {
        Long id = ++sequence;
        TimeRow newTime = new TimeRow(id, time.startAt());
        store.put(id, newTime);
        return newTime;
    }

    @Override
    public int delete(Long id) {
        TimeRow remove = store.remove(id);

        if (remove == null) {
            return 0;
        }

        return 1;
    }

    @Override
    public boolean existsByStartAt(LocalTime startAt) {
        return store.values()
                .stream()
                .anyMatch(theme -> theme.startAt().equals(startAt));
    }
}
