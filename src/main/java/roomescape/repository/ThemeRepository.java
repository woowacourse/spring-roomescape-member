package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    List<Theme> findFamous(long days, LocalDate date, long limit);

    Optional<Theme> findById(long themeId);

    Theme save(Theme theme);

    void deleteById(long themeId);

    boolean existsById(long themeId);
}
