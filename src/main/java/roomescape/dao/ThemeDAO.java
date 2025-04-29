package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Theme;

public interface ThemeDAO {

    long insert(Theme theme);

    List<Theme> findAll();

    Optional<Theme> findById(long id);

    boolean deleteById(long id);
}
