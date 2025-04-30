package roomescape.repository;

import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

public interface ThemeDao {

    boolean isExists(ThemeName name);

    Theme save(Theme theme);
}
