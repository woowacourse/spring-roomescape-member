package roomescape.repository;

import java.util.List;
import roomescape.entity.Theme;

public interface ThemeRepository {
    Theme save(Theme theme);

    List<Theme> findAll();
}
