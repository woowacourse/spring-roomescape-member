package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findByIsActive(boolean isActive);

    List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, int limit);

    Theme save(Theme theme);

    boolean updateStatus(Theme theme);

}
