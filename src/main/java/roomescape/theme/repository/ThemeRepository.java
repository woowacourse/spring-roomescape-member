package roomescape.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.theme.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    void deleteById(long id);

    List<Theme> findPopularThemes(LocalDate start, LocalDate end, int limit);
}
