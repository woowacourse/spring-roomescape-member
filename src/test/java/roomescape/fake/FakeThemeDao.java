package roomescape.fake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.domain.Theme;
import roomescape.persistence.dao.ThemeDao;

public class FakeThemeDao implements ThemeDao {

    private final List<Theme> themes;
    private final AtomicLong atomicLong = new AtomicLong(1L);

    public FakeThemeDao() {
        this.themes = new ArrayList<>();
    }

    @Override
    public Theme insert(final Theme theme) {
        long id = atomicLong.getAndIncrement();
        final Theme insertTheme = new Theme(id, theme.getName(), theme.getDescription(), theme.getThumbnail());
        themes.add(insertTheme);
        return insertTheme;
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean deleteById(final Long id) {
        final int beforeSize = themes.size();
        themes.removeIf(theme -> theme.getId().equals(id));
        final int afterSize = themes.size();
        final int deletedCount = beforeSize - afterSize;
        return deletedCount >= 1;
    }

    @Override
    public boolean existsById(final Long themeId) {
        return themes.stream()
                .anyMatch(theme -> theme.getId().equals(themeId));
    }

    @Override
    public boolean existsByName(final String name) {
        return themes.stream()
                .anyMatch(theme -> theme.getName().equals(name));
    }

    @Override
    public List<Theme> findPopularThemesBetween(final String startDate, final String endDate) {
        return null;
    }
}
