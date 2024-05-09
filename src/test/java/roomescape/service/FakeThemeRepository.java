/*
package roomescape.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {
    private final Map<Long, Theme> fakeThemeDB = new HashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public List<Theme> findAll() {
        return fakeThemeDB.values().stream().toList();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return Optional.ofNullable(fakeThemeDB.get(id));
    }

    @Override
    public Theme save(Theme theme) {
        long idx = index.getAndIncrement();
        Theme savedTheme = new Theme(idx, theme.getName(), theme.getDescription(), theme.getThumbnail());
        fakeThemeDB.put(idx, savedTheme);
        return savedTheme;
    }

    @Override
    public void deleteById(Long id) {
        fakeThemeDB.remove(id);
    }

    public void deleteAll() {
        fakeThemeDB.clear();
    }
}
*/
