package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;
import roomescape.domain.Themes;

public interface ThemeRepository {
    Themes findAll();

    Themes findAndOrderByPopularity(LocalDate start, LocalDate end, int count);

    Optional<Theme> findById(long id);

    Theme save(Theme theme);

    void delete(long id);
}
