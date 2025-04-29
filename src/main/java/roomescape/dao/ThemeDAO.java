package roomescape.dao;

import java.util.List;
import roomescape.domain.Theme;

public interface ThemeDAO {

    List<Theme> findAll();
}
