package roomescape.dao.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.Theme;

public interface ThemeDao {

    List<Theme> findAllTheme();

    void saveTheme(Theme theme);

    void deleteTheme(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAllThemeOfRankBy(LocalDate startDate, LocalDate currentDate, int limitCount);
}
