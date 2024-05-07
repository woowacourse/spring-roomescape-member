package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAllThemes();

    Optional<Theme> findById(Long id);

    List<Theme> findTopReservedThemesByDateRangeAndLimit(LocalDate start, LocalDate end, int limit);

    void delete(Long id);
}
