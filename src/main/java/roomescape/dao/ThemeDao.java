package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAllTheme();

    void saveTheme(Theme theme);

    void deleteTheme(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAllThemeOfRanks(LocalDate startDate, LocalDate currentDate);
}
