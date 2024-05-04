package roomescape.repository.theme;

import java.time.LocalDate;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    void delete(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findTrendings(LocalDate start, LocalDate end, Long limit);
}
