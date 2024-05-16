package roomescape.domain.theme.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {
    public Map<Long, Theme> themes = new HashMap<>();
    AtomicLong atomicLong = new AtomicLong(0);


    @Override
    public List<Theme> findAll() {
        return themes.values().stream().toList();
    }

    @Override
    public Theme insert(Theme theme) {
        long id = atomicLong.incrementAndGet();
        Theme savedTheme = new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.put(id, savedTheme);
        return savedTheme;
    }

    @Override
    public void deleteById(Long id) {
        themes.remove(id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        if (themes.containsKey(id)) {
            return Optional.of(themes.get(id));
        }
        return Optional.empty();
    }
}
