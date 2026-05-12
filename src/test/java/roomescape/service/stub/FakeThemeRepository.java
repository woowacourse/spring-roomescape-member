package roomescape.service.stub;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final List<Theme> storage = new ArrayList<>();
    private long sequence = 1L;
    private final List<Theme> popularThemes = List.of();

    @Override
    public List<Theme> findAll() {
        return new ArrayList<>(storage);
    }

    @Override
    public Optional<Theme> findById(final long id) {
        return storage.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(final long id) {
        storage.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Theme save(final Theme theme) {
        Theme saved = theme;
        if (theme.getId() == null) {
            saved = theme.withId(sequence++);
        }
        storage.add(saved);
        return saved;
    }

    @Override
    public boolean existsByName(final String name) {
        return storage.stream()
                .anyMatch(theme -> theme.getName().equals(name));
    }

    @Override
    public List<Theme> findPopularThemes(final int period, final int limit, final LocalDate now) {
        return popularThemes.stream().limit(limit).toList();
    }

}
