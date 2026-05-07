package roomescape.repository.theme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    Theme createTheme(Theme theme);

    void deleteById(long id);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    List<Theme> findWeekPopularThemesOrderByRank(int limit);
}
