package roomescape.repository.theme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeCommand;
import roomescape.domain.theme.ThemeWithCount;

public interface ThemeRepository {
    Theme addTheme(ThemeCommand themeCommand);
    List<Theme> getAllTheme();
    Optional<Theme> getTheme(long id);
    void deleteTheme(long id);
    List<ThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition);
}
