package roomescape.reservation.domain.repository;

import java.util.List;
import roomescape.reservation.domain.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);
}
