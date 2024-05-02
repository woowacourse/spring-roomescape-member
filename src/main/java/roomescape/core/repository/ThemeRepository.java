package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Theme;

public interface ThemeRepository {

    Long save(Theme theme);

    List<Theme> findAll();

    List<Theme> findPopular();

    Theme findById(long id);

    Integer countByName(String name);

    void deleteById(long id);
}
