package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {
    Theme create(Theme theme);

    Theme read(Long id);

    List<Theme> readAll();

    void delete(Long id);
}
