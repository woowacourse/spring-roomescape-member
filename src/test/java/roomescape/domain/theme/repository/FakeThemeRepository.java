package roomescape.domain.theme.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.theme.entity.Theme;

public class FakeThemeRepository implements ThemeRepository {

    private final AtomicLong id = new AtomicLong(0);
    private final List<Theme> themes = new ArrayList<>();

    @Override
    public List<Theme> findAllThemes() {
        return List.of();
    }

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = Theme.reconstruct(id.addAndGet(1), theme.getName(), theme.getDescription(),
            theme.getImageUrl());
        themes.add(savedTheme);
        return savedTheme;
    }

    @Override
    public void deleteThemeById(Long id) {
        themes.removeIf(time -> Objects.equals(time.getId(), id));
    }
}
