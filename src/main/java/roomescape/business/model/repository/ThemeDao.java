package roomescape.business.model.repository;

import java.util.List;
import java.util.Optional;
import roomescape.business.model.entity.theme.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme save(Theme theme);

    int deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> sortByRank();

    boolean existByName(String name);
}
