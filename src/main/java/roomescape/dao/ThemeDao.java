package roomescape.dao;

import roomescape.dao.row.ThemeRow;

public interface ThemeDao extends CommonDao<ThemeRow> {
    boolean existsByName(String name);
}
