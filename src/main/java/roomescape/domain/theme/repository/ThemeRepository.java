package roomescape.domain.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme save(Theme theme);

    void deleteThemeById(Long id);

    Optional<Theme> findThemeById(Long id);
}
