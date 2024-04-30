package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Theme;

public interface ThemeRepository {
    Long save(Theme theme);

    List<Theme> findAll();

    Theme findById(long id);

    Integer countByName(String name);

    Theme deleteById(long id);
}
