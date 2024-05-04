package roomescape.theme.dao;

import java.util.List;
import roomescape.theme.domain.Theme;

public interface ThemeDao {

    Theme save(Theme theme);

    List<Theme> findAllThemes();

    void deleteById(long themeId);

    Theme findById(long themeId);
}
