package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;

public interface ThemeDao {

    Theme save(Theme theme);

    List<Theme> findAllThemes();

    Optional<Theme> findById(Long id);

    List<Theme> findTopReservedThemesByDateRangeAndLimit(LocalDate start, LocalDate end, int limit);

    void delete(Long id);
}
