package roomescape.theme.repository;

import roomescape.theme.entity.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ThemeRepository {
    Theme save(Theme entity);

    List<Theme> findAll();

    boolean deleteById(Long id);

    Optional<Theme> findById(Long id);

    Optional<Theme> findByName(String name);

    List<Theme> findPopularThemesByDateRangeAndLimit(LocalDate startDate, LocalDate endDate, int limit);
}
