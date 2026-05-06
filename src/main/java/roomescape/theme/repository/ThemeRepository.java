package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.domain.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    void deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    boolean existsByName(String name);

    List<AvailableTimeQueryResult> findAvailableTimes(Long themeId, LocalDate date);
}
