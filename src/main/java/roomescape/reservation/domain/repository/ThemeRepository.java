package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    void deleteById(Long id);

    List<Theme> findThemeRanking(LocalDate startDate, LocalDate endDate, int limit);
}
