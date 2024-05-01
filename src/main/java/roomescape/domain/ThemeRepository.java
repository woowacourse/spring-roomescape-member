package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme create(Theme theme);

    void removeById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, int limitCount);
}
