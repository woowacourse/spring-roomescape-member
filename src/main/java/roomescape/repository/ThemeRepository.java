package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme save(final Theme theme);

    List<Theme> findAll();

    List<Theme> findPopularThemesByPeriod(final long periodDay);

    Theme findById(final long id);

    boolean hasDuplicateTheme(final String name);

    void deleteById(final long id);
}
