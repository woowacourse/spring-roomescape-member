package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.DateRange;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;

public interface ThemeRepository {

    Theme save(ThemeName name, ThemeDescription description, ThemeThumbnail thumbnail);

    void deleteById(Long id);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> findPopularThemeDuringAWeek(long limit, DateRange dateRange);
}
