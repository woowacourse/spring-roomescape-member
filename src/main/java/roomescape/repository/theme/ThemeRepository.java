package roomescape.repository.theme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ReservationThemeCommand;
import roomescape.domain.theme.ReservationThemeWithCount;

public interface ThemeRepository {
    Theme addTheme(ReservationThemeCommand reservationThemeCommand);
    List<Theme> getAllTheme();
    Optional<Theme> getTheme(long id);
    void deleteTheme(long id);
    List<ReservationThemeWithCount> getPopularTheme(PopularThemeCondition popularThemeCondition);
}
