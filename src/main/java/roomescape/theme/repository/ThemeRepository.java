package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.entity.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findPopularThemes(LocalDate startDateInclusive, LocalDate endDateExclusive, int limit);

    int deleteById(Long id);

}
