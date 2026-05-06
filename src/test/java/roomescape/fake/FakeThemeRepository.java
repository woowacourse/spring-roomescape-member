package roomescape.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    private final Map<Long, Theme> themes = new HashMap<>();
    private Long idHoler = 1L;

    @Override
    public Optional<Theme> findById(Long id) {
        return Optional.of(themes.get(id));
    }

    @Override
    public List<Theme> findAll() {
        return List.of();
    }

    @Override
    public Theme save(Theme theme) {
        Theme savedTheme = theme.withId(idHoler);
        themes.put(idHoler++, savedTheme);
        return savedTheme;
    }

    @Override
    public Integer delete(long id) {
        return 0;
    }

    @Override
    public Boolean existsByNameAndDescription(Theme theme) {
        return themes.values().stream()
                .anyMatch(savedTheme -> savedTheme.equals(theme));
    }
}
