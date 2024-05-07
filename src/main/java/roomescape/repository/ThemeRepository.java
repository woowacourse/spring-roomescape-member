package roomescape.repository;

import java.time.LocalDate;
import java.util.List;

import roomescape.model.Theme;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme addTheme(Theme theme);

    void deleteTheme(long id);

    List<Theme> findThemeRankingByDate(LocalDate before, LocalDate after, int limit);
}
