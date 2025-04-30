package roomescape.test.fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {
    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public Optional<Theme> findById(final long id) {
        if (!themes.containsKey(id)) {
            return Optional.empty();
        }
        return Optional.of(themes.get(id));
    }

    @Override
    public long addTheme(final Theme theme) {
        Theme newTheme = new Theme(
                index.getAndIncrement(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.put(newTheme.getId(), newTheme);
        return newTheme.getId();
    }

    @Override
    public List<Theme> findAll() {
        return themes.values().stream().toList();
    }

    @Override
    public void deleteById(final long id) {
        themes.remove(id);
    }
}
