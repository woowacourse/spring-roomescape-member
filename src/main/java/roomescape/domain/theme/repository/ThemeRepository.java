package roomescape.domain.theme.repository;

import java.util.List;
import roomescape.domain.theme.entity.Theme;

public interface ThemeRepository {

    List<Theme> findAllThemes();

    Theme save(Theme theme);
}
