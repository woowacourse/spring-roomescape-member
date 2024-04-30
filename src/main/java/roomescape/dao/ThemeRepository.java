package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();
}
