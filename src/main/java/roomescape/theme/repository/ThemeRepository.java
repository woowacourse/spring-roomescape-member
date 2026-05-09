package roomescape.theme.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.entity.Theme;

public interface ThemeRepository {

    Theme save(Theme theme);

    Optional<Theme> findById(Long id);

    List<Theme> findAll();

    List<Theme> findPopularThemes(int days, int limit);

    void deleteById(Long id);
}
