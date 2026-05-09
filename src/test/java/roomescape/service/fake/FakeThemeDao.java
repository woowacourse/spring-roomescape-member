package roomescape.service.fake;

import roomescape.dao.ThemeDao;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeThemeDao implements ThemeDao {
    private final Map<Long, ThemeRow> store = new HashMap<>();

    private long sequence = 0L;


    @Override
    public List<ThemeRow> findAll() {
        return store.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<ThemeRow> findById(Long id) {
        ThemeRow theme = store.get(id);

        if (theme == null) {
            return Optional.empty();
        }
        return Optional.of(theme);
    }

    @Override
    public ThemeRow create(ThemeRow theme) {
        Long id = ++sequence;
        ThemeRow newTheme = new ThemeRow(id, theme.name(), theme.thumbnailUrl(), theme.description());
        store.put(id, newTheme);
        return newTheme;
    }

    @Override
    public int delete(Long id) {
        ThemeRow remove = store.remove(id);
        if (remove == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean existsByName(String name) {
        return store.values()
                .stream()
                .anyMatch(theme -> theme.name().equals(name));
    }

    @Override
    public List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate) {
        return List.of();
    }

    @Override
    public List<ThemeRow> findPopulars(int limit, int days, LocalDate date) {
        return List.of();
    }


}
