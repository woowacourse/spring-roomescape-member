package roomescape.repository.theme;

import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository {

    Theme save(Theme theme);

    boolean hasSameTheme(Theme theme);

    void delete(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findPopularThemes(int beforeDays, int count);

    List<Theme> findAll();
}
