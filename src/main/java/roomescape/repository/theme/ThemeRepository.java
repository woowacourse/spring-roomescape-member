package roomescape.repository.theme;

import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme createTheme(Theme theme);

    void deleteById(Long id);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> findTop10WeekPopularThemesOrderByRank();
}
