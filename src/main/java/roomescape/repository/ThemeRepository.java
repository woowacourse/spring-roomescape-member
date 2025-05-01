package roomescape.repository;

import roomescape.entity.Theme;

import java.util.List;

public interface ThemeRepository {

    Theme findById(Long id);

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long id);

    List<Theme> findPopularThemesThisWeek();
}
