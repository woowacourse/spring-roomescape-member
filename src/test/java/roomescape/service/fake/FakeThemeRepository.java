package roomescape.service.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {
    private final List<Theme> themes = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = new Theme(
                index.getAndIncrement(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl()
        );
        themes.add(savedTheme);
        return savedTheme;
    }

    @Override
    public boolean existByName(String name) {
        return themes.stream()
                .anyMatch(theme -> theme.getName().equals(name));
    }
}
