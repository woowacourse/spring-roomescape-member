package roomescape.service.fake;

import roomescape.dao.TimeDao;
import roomescape.dao.vo.TimeRow;
import roomescape.dao.vo.TimeRows;
import roomescape.domain.Time;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeTimeDao implements TimeDao {

    private final Map<Long, Time> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Long insert(Time time) {
        Long id = ++sequence;
        store.put(id, new Time(id, time.getStartAt()));
        return id;
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
