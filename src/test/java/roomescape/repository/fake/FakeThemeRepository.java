package roomescape.repository.fake;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> themes = new CopyOnWriteArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Theme save(Theme theme) {
        Long id = idGenerator.getAndIncrement();
        Theme saved = new Theme(
                id,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl(),
                theme.isActive()
        );
        themes.add(saved);
        return saved;
    }

    @Override
    public void update(Theme theme) {
        for (int i = 0; i < themes.size(); i++) {
            Theme savedTheme = themes.get(i);

            if (savedTheme.getId().equals(theme.getId())) {
                themes.set(i, theme);
                return;
            }
        }
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themes.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Theme> findAllActiveThemesByPaging(int page, int size) {
        int offset = page * size;

        return themes.stream()
                .filter(Theme::isActive)
                .sorted(Comparator.comparing(Theme::getId))
                .skip(offset)
                .limit(size)
                .toList();
    }

    @Override
    public List<Theme> findTopThemesByReservationCount(LocalDate startDate, LocalDate endDate, int limit) {
        return themes.stream()
                .filter(Theme::isActive)
                .sorted(Comparator.comparing(Theme::getId))
                .limit(limit)
                .toList();
    }

    @Override
    public boolean isActiveByName(String name) {
        return themes.stream()
                .anyMatch(theme ->
                        theme.getName().equals(name)
                                && theme.isActive()
                );
    }
}
