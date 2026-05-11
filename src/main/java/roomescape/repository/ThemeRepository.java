package roomescape.repository;

import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    void delete(Long id);

    Optional<Theme> findById(Long id);

    List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date);

    List<Theme> findPopularThemes(LocalDate startInclusive, LocalDate endInclusive, int limit);
}
