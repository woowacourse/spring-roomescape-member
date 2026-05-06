package roomescape.dao;

import roomescape.domain.Theme;
import roomescape.domain.vo.Name;

public interface ThemeDao extends CommonDao<Theme> {
    boolean existsByName(Name name);
}
