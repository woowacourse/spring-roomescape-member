package roomescape.repository;

import java.util.List;
import roomescape.entity.Theme;

public interface ThemeRepository {

    Theme findById(Long id);

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long id);

    List<Theme> findPopularThemesThisWeek();

    boolean existsByName(String name);
}
