package roomescape.repository;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {
    List<Theme> findAll();

    Theme save(Theme theme);

    void delete(long id);
}
