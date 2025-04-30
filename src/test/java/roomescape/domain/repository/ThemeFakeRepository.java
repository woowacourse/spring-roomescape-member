package roomescape.domain.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ThemeRepository;

public class ThemeFakeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Long saveAndReturnId(Theme themeWithoutId) {
        long id = idGenerator.incrementAndGet();
        Theme theme = themeWithoutId.withId(id);
        themes.put(id, theme);

        return id;
    }

    @Override
    public List<Theme> findAll() {
        return themes.values().stream()
                .toList();
    }

    @Override
    public int deleteById(Long id) {
        if (themes.containsKey(id)) {
            themes.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        if(themes.containsKey(id)){
            return Optional.of(themes.get(id));
        }
        return Optional.empty();
    }
}
