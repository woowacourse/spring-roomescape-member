package roomescape.persistence.dao;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Theme;

public interface ThemeDao {
    Long insert(Theme theme);

    List<Theme> findAll();

    boolean deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> findPopularThemesBetween(String startDate, String endDate);

    boolean existsByName(String name);

    boolean existsById(Long themeId);
}
