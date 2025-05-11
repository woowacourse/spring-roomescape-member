package roomescape.stub;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;
import roomescape.dto.response.PopularThemeResponse;
import roomescape.repository.ThemeRepository;

public class StubThemeRepository implements ThemeRepository {

    private final List<Theme> data = new ArrayList<>();
    private final AtomicLong idSequence = new AtomicLong();

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
        return List.of(); // TODO. Stub이기 때문에 Setter로 구현하는 방식을 어떻게 생각하시는지 지노에게 여쭤보기
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
