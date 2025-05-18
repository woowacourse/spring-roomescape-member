package roomescape.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.Setter;
import roomescape.domain.entity.Theme;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.response.PopularThemeResponse;

public class StubThemeRepository implements ThemeRepository {

    private final List<Theme> data = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong();
    @Setter
    private List<Long> popularThemeIds = List.of();

    public StubThemeRepository(Theme... initialThemes) {
        data.addAll(List.of(initialThemes));
        long maxId = data.stream()
                .mapToLong(Theme::getId)
                .max()
                .orElse(0L);
        idSequence.set(maxId);
    }

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = new Theme(idSequence.incrementAndGet(), theme.getName(),
                theme.getDescription(), theme.getThumbnail());
        data.add(savedTheme);
        return savedTheme;
    }

    @Override
    public List<Theme> findAll() {
        return List.copyOf(data);
    }

    @Override
    public List<PopularThemeResponse> findAllPopular() {
        return popularThemeIds.stream()
                .map(this::findById)
                .flatMap(Optional::stream)
                .map(theme -> new PopularThemeResponse(theme.getName(), theme.getDescription(), theme.getThumbnail()))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(theme -> theme.getId().equals(id));
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return data.stream()
                .filter(theme -> theme.getId().equals(id))
                .findFirst();
    }
}
