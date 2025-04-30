package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.model.Theme;

public interface ThemeDao {
    Theme save(Theme theme);

    boolean deleteById(Long id);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    List<Theme> getTopTenTheme();
}
