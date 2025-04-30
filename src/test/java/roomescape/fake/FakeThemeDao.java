package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ThemeDao;
import roomescape.model.Theme;

public class FakeThemeDao implements ThemeDao {

    private final Map<Long, Theme> themes = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = new Theme(index.getAndIncrement(), theme.getName(), theme.getDescription(),
                theme.getThumbnail());
        themes.put(savedTheme.getId(), savedTheme);
        return savedTheme;
    }

    @Override
    public boolean deleteById(Long id) {
        return themes.remove(id) != null;
    }

    @Override
    public List<Theme> findAll() {
        return new ArrayList<>(themes.values());
    }

    @Override
    public Optional<Theme> findById(Long id) {
        Theme theme = themes.get(id);
        return Optional.ofNullable(theme);
    }

    @Override
    public List<Theme> getTopTenTheme() {
        return List.of();
    }
}
