package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.entity.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    boolean deleteById(Long id);

    Optional<Theme> findById(Long id);

    Optional<Theme> findByName(String name);

    List<Theme> findPopularDescendingUpTo(LocalDate startDate, LocalDate endDate, int limit);
}
