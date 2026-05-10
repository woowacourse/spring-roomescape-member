package roomescape.support.fake;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;

public class FakeThemeRepository implements ThemeRepository {

    public List<Theme> findAllResult = List.of();
    public Theme theme;
    public Theme savedTheme;
    public Long deletedId;

    @Override
    public List<Theme> findAll() {
        return findAllResult;
    }

    @Override
    public Optional<Theme> findById(Long id) {
        if (theme != null) {
            return Optional.of(theme);
        }
        return findAllResult.stream()
            .filter(candidate -> candidate.getId().equals(id))
            .findFirst();
    }

    @Override
    public Theme save(Theme theme) {
        savedTheme = theme;
        return Theme.of(1L, theme.getName(), theme.getContent(), theme.getUrl());
    }

    @Override
    public int deleteById(Long id) {
        deletedId = id;
        return 1;
    }
}
