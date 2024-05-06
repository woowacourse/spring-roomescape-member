package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme findById(Long id);

    List<Theme> findTopThemesWithinDays(LocalDate date, int limit);

    Theme save(Theme theme);

    void deleteById(Long id);
}
