package roomescape.theme.business.model.repository;

import java.util.List;
import java.util.Optional;
import roomescape.theme.business.model.entity.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme save(Theme theme);

    int deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> sortByRank();

    boolean existByName(String name);
}
