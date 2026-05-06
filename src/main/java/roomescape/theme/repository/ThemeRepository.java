package roomescape.theme.repository;

import roomescape.theme.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    void deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    boolean existsByName(String name);

    List<Theme> findPopularThemes(int period, int limit);
}
