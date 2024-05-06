package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {
    List<Theme> findAll();

    Theme findById(long id);

    List<Theme> findThemesByDescOrder();

    long save(Theme theme);

    boolean existByName(String name);

    void deleteById(Long id);
}
