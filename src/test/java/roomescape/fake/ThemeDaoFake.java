package roomescape.fake;

import roomescape.domain.model.Theme;
import roomescape.infrastructure.dao.ThemeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ThemeDaoFake implements ThemeDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, Theme> THEMES = new HashMap<>();

    @Override
    public Theme findById(Long id) {
        return THEMES.get(id);
    }

    @Override
    public Theme save(Theme theme) {
        Long id = IDX.getAndIncrement();
        Theme newTheme = new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
        THEMES.put(id, newTheme);
        return newTheme;
    }

    @Override
    public List<Theme> findAll() {
        return THEMES.values().stream().toList();
    }

    @Override
    public int deleteById(Long id) {
        if (THEMES.containsKey(id)) {
            THEMES.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean existByName(String name) {
        long count = THEMES.values().stream()
                .filter(theme -> theme.getName().equals(name))
                .count();
        return count != 0;
    }

    @Override
    public List<Theme> findPopular(final int count) {
        return List.of();
    }

    public void clear() {
        THEMES.clear();
        IDX.set(0);
    }
}
