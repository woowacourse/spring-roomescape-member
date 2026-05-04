package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDao {
    Theme read(Long id);

    List<Theme> readAll();
}
