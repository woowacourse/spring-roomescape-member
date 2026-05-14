package roomescape.dao;

import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;

import java.time.LocalDate;
import java.util.List;

public interface ThemeDao extends CommonDao<ThemeRow> {
    boolean existsByName(String name);
}
