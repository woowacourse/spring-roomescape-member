package roomescape.reservation.domain.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    boolean deleteById(long themeId);

    Optional<Theme> findById(long themeId);
}
