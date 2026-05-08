package roomescape.service.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.CommonDao;

public abstract class FakeCommonDao<T> implements CommonDao<T> {
    protected final Map<Long, T> store = new HashMap<>();
    protected final AtomicLong id = new AtomicLong(1);

    @Override
    public List<T> findAll() {
        return store.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public T insert(T t) {
        Long saveId = id.getAndIncrement();
        T saveDomain = create(t, saveId);
        store.put(saveId, saveDomain);
        return saveDomain;
    }

    @Override
    public int delete(Long id) {
        T removed = store.remove(id);
        if (removed == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    abstract T create(T t, Long id);
}
