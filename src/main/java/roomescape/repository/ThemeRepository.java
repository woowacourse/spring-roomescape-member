package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {

    List<Theme> findAllTheme();

    void saveTheme(Theme theme);

    void deleteTheme(Long id);

    Theme findById(Long id);

    List<Theme> findAllThemeOfRanks(LocalDate startDate, LocalDate currentDate);
}
