package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.response.ThemeReservationTimeResponse;

public interface ThemeRepository {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<ThemeReservationTimeResponse> findAllThemeReservationTimesByThemeIdAndDate(Long themeId, LocalDate date);

    Theme save(Theme theme);

    void deleteById(Long id);
}
