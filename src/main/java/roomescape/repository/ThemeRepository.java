package roomescape.repository;

import roomescape.model.Theme;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme addTheme(Theme theme);

    void deleteTheme(long id);
}
