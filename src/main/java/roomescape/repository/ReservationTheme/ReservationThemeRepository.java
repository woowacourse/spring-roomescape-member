package roomescape.repository.ReservationTheme;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme.Theme;
import roomescape.domain.Theme.ThemeCommand;

public interface ReservationThemeRepository {
    Theme addTheme(ThemeCommand themeCommand);
    List<Theme> getAllTheme();
    Optional<Theme> getTheme(long id);
    void deleteTheme(long id);
}
