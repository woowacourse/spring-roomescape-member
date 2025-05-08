package roomescape.persistence.dao;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Theme;

public interface ThemeDao {

    Long insert(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    boolean deleteById(Long id);

    boolean existsById(Long themeId);

    boolean existsByName(String name);

    List<Theme> findPopularThemesBetween(String startDate, String endDate);
}
