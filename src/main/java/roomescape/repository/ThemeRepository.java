package roomescape.repository;

import java.util.List;
import roomescape.model.Theme;

public interface ThemeRepository {
    Theme addTheme(Theme theme);

    List<Theme> getAllTheme();

    int deleteTheme(Long id);

    Theme findById(Long id);

    List<Theme> findAllByIdIn(List<Long> ids);
}
