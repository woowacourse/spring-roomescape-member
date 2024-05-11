package roomescape.domain.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme theme);

    Optional<Theme> findById(long id);

    List<Theme> findAll();

    boolean deleteById(long id);

    List<Theme> findPopularThemesForWeekLimit10(LocalDate now);
}
