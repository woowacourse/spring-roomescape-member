package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> findPopularThemes(LocalDate startDate, LocalDate endDate, Long limit);

    void delete(Long id);
}
