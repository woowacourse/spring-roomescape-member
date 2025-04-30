package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAllThemes();

    Theme addTheme(Theme theme);

    void removeThemeById(Long id);

    Theme findThemeById(Long id);

    List<Theme> findTopReservedThemesInPeriodWithLimit(int period, int limitCount);
}
