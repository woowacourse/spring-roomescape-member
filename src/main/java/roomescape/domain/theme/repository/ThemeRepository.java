package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<ThemeReservationTimeResult> findAllReservationTimesByThemeIdAndDate(Long themeId, LocalDate date);

    List<PopularThemeResult> findPopularThemes(LocalDate startDate, LocalDate endDate, Integer limit);

    Theme save(Theme theme);

    void deleteById(Long id);
}
