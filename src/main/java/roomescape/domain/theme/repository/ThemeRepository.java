package roomescape.domain.theme.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.theme.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    boolean existsByName(String name);

    Optional<Theme> findById(long id);

    List<Theme> findAll();

    void deleteById(long id);

    List<Theme> findPopularThemesForWeekLimit10(LocalDate now);
}
