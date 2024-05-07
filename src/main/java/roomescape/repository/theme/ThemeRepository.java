package roomescape.repository.theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public interface ThemeRepository {

    Theme save(Theme theme);

    void delete(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findTrendings(LocalDate start, LocalDate end, Long limit);

    boolean hasTheme(ThemeName name);
}
