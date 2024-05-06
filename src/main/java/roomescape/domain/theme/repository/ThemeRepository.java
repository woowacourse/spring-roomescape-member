package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    Optional<Theme> findById(long id);

    List<Theme> findAll();

    int deleteById(long id);

    List<Theme> findPopularThemesForWeekLimit10(LocalDate now);
}
