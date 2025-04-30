package roomescape.reservation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes;
    private AtomicLong index = new AtomicLong(0);

    public FakeThemeRepository() {
        this.themes = new ArrayList<>();
    }

    @Override
    public Long save(Theme theme) {
        long currentIndex = index.incrementAndGet();
        themes.add(new Theme(currentIndex, theme.getName(), theme.getDescription(), theme.getThumbnail()));
        return currentIndex;
    }

    @Override
    public Theme findById(Long id) {
        return themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findAny()
                .orElseThrow();
    }

    @Override
    public List<Theme> findAll() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public int deleteById(Long id) {
        Optional<Theme> findTheme = themes.stream()
                .filter(theme -> Objects.equals(theme.getId(), id))
                .findAny();

        if (findTheme.isEmpty()) {
            return 0;
        }

        Theme theme = findTheme.get();
        themes.remove(theme);
        return 1;
    }
}
