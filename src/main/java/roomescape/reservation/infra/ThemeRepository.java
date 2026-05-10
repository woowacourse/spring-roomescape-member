package roomescape.reservation.infra;

import java.util.Optional;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {
    Theme save(Theme domain);

    void deleteById(long id);

    List<Theme> findScheduledThemesByDate(LocalDate date);

    List<Theme> findPopularThemeByCurrentDate(LocalDate currentDate);

    Optional<Theme> findById(long id);

    List<Theme> findAll();
}
