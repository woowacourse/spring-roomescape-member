package roomescape.business.model.repository;

import java.util.List;
import java.util.Optional;
import roomescape.business.model.entity.Theme;

public interface ThemeDao {

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long id);

    Optional<Theme> findById(Long id);

    List<Theme> sortByRank();

    boolean existByName(String name);
}
