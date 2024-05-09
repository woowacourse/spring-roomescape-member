package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(long id);

    boolean existsByName(String name);

    Optional<Theme> findById(long id);

    List<Theme> findPopularThemes(LocalDate now);
}
