package roomescape.theme.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme theme);
    void delete(Long id);
    boolean existsThemeById(Long id);
    Optional<Theme> findById(Long id);
    List<Theme> findAll();
    List<Theme> findByReservationCountWithLimit(LocalDate startDate, LocalDate endDate, int limit);
}
