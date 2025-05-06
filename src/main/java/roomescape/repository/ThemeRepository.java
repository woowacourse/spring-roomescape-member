package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.Theme;

public interface ThemeRepository {
    Theme addTheme(Theme theme);

    List<Theme> getAllThemes();

    int deleteTheme(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAllByIdIn(List<Long> ids);
}
