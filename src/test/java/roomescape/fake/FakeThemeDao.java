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
    public Long save(final Theme theme) {
        long id = atomicLong.getAndIncrement();
        final Theme savedTheme = new Theme(
                id,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
        themes.add(savedTheme);
        return id;
    }

    @Override
    public Optional<Theme> find(final Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Theme> findPopularThemesBetween(final String startDate, final String endDate) {
        return List.of();
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public boolean remove(final Long id) {
        final int beforeSize = themes.size();
        themes.removeIf(theme -> theme.getId().equals(id));
        final int afterSize = themes.size();
        final int deletedCount = beforeSize - afterSize;
        return deletedCount >= 1;
    }

    @Override
    public boolean existsByName(final String name) {
        return false;
    }
}
