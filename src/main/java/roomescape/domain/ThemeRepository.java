package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme insertTheme(Theme theme);

    void deleteThemeById(long id);

    boolean isThemeExistsById(long themeId);

    List<Theme> findTopPopularThemes(LocalDate startDate, LocalDate endDate, int themeount);
}
