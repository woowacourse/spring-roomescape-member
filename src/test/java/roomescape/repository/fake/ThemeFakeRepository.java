package roomescape.repository.fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;

public class ThemeFakeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public List<Theme> findAll() {
        return List.copyOf(themes.values());
    }

    @Override
    public Long save(final Theme theme) {
        var id = index.getAndIncrement();
        var created = new Theme(id, theme.name(), theme.description(), theme.thumbnail());
        themes.put(id, created);
        return id;
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        return Optional.ofNullable(themes.get(id));
    }

    @Override
    public Boolean removeById(final Long id) {
        Theme removed = themes.remove(id);
        return removed != null;
    }
}
