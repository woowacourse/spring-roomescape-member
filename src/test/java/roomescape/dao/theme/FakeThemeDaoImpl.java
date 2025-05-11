package roomescape.dao.theme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.theme.Theme;

public class FakeThemeDaoImpl implements ThemeDao {

    private final List<Theme> themes = new ArrayList<>();
    private final AtomicLong id = new AtomicLong(1);

    @Override
    public List<Theme> findAllTheme() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public void saveTheme(final Theme theme) {
        theme.setId(id.getAndIncrement());
        themes.add(theme);
    }

    @Override
    public void deleteTheme(final Long id) {
        themes.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Theme> findAllThemeOfRankBy(final LocalDate startDate, final LocalDate currentDate,
                                            final int limitCount) {
        return List.of();
    }
}
