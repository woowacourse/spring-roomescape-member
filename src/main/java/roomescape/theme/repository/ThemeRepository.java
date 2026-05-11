package roomescape.theme.repository;

import roomescape.theme.Theme;

import java.util.Optional;

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
