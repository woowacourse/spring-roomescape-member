package roomescape.repository;

import roomescape.model.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme addTheme(Theme theme);

    void deleteTheme(long id);

    Theme findThemeById(long id);

    List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit);
}
