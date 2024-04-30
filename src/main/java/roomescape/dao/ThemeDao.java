package roomescape.dao;

import java.util.List;
import roomescape.domain.theme.Theme;

public interface ThemeDao {

    List<Theme> readAll();

    Theme create(Theme theme);
}
