package roomescape.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;

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

    }

    @Override
    public Optional<Theme> findById(final Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Theme> findAllThemeOfRanks(final LocalDate startDate, final LocalDate currentDate) {
        return List.of();
    }
}
