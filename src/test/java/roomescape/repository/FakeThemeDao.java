package roomescape.repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import roomescape.domain.Theme;

public class FakeThemeDao implements ThemeRepository {

    private final Map<Long, Theme> storage = new HashMap<>();
    private long sequence = 1L;

    @Override
    public List<Theme> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<Theme> findById(long id) {
        return storage.get(id);
    }

    @Override
    public Theme save(Theme time) {
        long id = sequence++;
        Theme savedTheme = new Theme(id, null, null, null);
        storage.put(id, savedTheme);
        return savedTheme;
    }

    @Override
    public void deleteById(long id) {
        storage.remove(id);
    }

    @Override
    public List<Theme> findPopularThemes(Long topCount, LocalDate fromDate, LocalDate toDate) {
        return List.of();
    }
}
