package roomescape.theme.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.theme.domain.Theme;

public class FakeThemeRepository implements ThemeRepository {
    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Theme> getAll() {
        return themes.entrySet().stream()
                .map(entry -> {
                    Theme value = entry.getValue();
                    return Theme.of(entry.getKey(), value.getName(), value.getDescription(), value.getThumbnail());
                })
                .toList();
    }

    @Override
    public Theme put(final Theme theme) {
        long id = index.getAndIncrement();
        themes.put(id, theme);
        return Theme.of(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    @Override
    public boolean deleteById(final long id) {
        return themes.remove(id) != null;
    }

    @Override
    public Optional<Theme> findById(final long id) {
        return Optional.ofNullable(themes.get(id));
    }
}
