package roomescape.repository.theme;

import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme createTheme(Theme theme);

    void deleteById(long id);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> findWeekPopularThemesOrderByRank(int limit);
}
