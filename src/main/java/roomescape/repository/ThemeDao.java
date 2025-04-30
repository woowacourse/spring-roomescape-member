package roomescape.repository;

import java.util.List;
import roomescape.service.reservation.Theme;
import roomescape.service.reservation.ThemeName;

public interface ThemeDao {

    boolean isExists(ThemeName name);

    Theme save(Theme theme);

    List<Theme> findAll();

    Theme findById(long id);
}
