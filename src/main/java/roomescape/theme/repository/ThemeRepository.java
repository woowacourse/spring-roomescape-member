package roomescape.theme.repository;

import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {

    Theme findById(Long id);

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long id);

    List<Theme> findPopularThemesThisWeek(LocalDate startInclusive, LocalDate endInclusive, int count);
}
