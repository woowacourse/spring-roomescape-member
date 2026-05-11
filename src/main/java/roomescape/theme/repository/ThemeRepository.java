package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    void deleteById(Long id);

    List<Theme> findPopularThemes(LocalDate start, LocalDate end);
}
