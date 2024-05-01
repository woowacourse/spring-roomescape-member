package roomescape.repository;

import roomescape.model.Theme;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme addTheme(Theme theme);

    long deleteTheme(long id);
}
