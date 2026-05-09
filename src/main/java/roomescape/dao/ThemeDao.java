package roomescape.dao;

import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;

import java.time.LocalDate;
import java.util.List;

public interface ThemeDao extends CommonDao<ThemeRow> {
    boolean existsByName(String name);

    List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate);

    List<ThemeRow> findPopulars(int limit, int days, LocalDate date);
}
