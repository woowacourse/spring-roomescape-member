package roomescape.theme.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

public class StubThemeRepository implements ThemeRepository {

    private final List<Theme> data = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong();

    public StubThemeRepository(final Theme... themes) {
        data.addAll(List.of(themes));
        long maxId = data.stream()
                .mapToLong(Theme::getId)
                .max()
                .orElse(0L);
        atomicLong.set(maxId);
    }

    @Override
    public Theme save(final Theme theme) {
        Theme savedTheme = new Theme(atomicLong.incrementAndGet(), theme.getName(),
                theme.getDescription(), theme.getThumbnail());
        data.add(savedTheme);
        return savedTheme;
    }

    @Override
    public List<Theme> findAll() {
        return List.copyOf(data);
    }

    @Override
    public void deleteById(final Long id) {
        data.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(final Long id) {
        return data.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }
}
