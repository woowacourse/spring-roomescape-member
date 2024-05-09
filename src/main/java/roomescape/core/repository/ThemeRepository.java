package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Theme;

public interface ThemeRepository {
    Long save(final Theme theme);

    List<Theme> findAll();

    List<Theme> findPopularThemesByPeriod(final long periodDay);

    Theme findById(final long id);

    boolean hasDuplicateTheme(final String name);

    void deleteById(final long id);
}
