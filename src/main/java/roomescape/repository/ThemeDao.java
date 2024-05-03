package roomescape.repository;

import roomescape.model.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeDao {

    List<Theme> findAllThemes();

    Theme saveTheme(Theme theme);

    void deleteThemeById(long id);

    Theme findThemeById(long id);

    List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit);
}
