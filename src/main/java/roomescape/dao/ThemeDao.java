package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {

    List<Theme> findAllTheme();

    void saveTheme(Theme theme);

    void deleteTheme(Long id);

}
