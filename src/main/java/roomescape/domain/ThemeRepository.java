package roomescape.domain;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme insertTheme(Theme theme);

    void deleteThemeById(long id);

    boolean isExistThemeOf(long themeId);
}
