package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeDAO {

    long insert(Theme theme);

    boolean existsByName(String name);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    boolean deleteById(long id);
}
