package roomescape.repository.Theme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme.PopularThemeCondition;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;
import roomescape.domain.Theme.ThemeWithCount;

public interface ThemeRepository {
    Theme addTheme(ThemeCommand themeCommand);
    List<Theme> getAllTheme();
    Optional<Theme> getTheme(long id);
    void deleteTheme(long id);
    List<ThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition);
}
