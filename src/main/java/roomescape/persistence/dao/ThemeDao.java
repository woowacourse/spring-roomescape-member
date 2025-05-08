package roomescape.persistence.dao;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Theme;

public interface ThemeDao {
    Long save(Theme theme);

    List<Theme> findAll();

    boolean remove(Long id);

    Optional<Theme> find(Long id);

    List<Theme> findPopularThemesBetweenWithLimit(String startDate, String endDate, int limit);
}
