package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    List<Theme> findAll();

    List<Theme> findPopular();

    Theme findById(long id);

    boolean existByName(String name);

    void deleteById(long id);
}
