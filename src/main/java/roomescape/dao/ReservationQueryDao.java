package roomescape.dao;

import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ThemeRow;

import java.time.LocalDate;
import java.util.List;

public interface ReservationQueryDao {
    public List<AvailableTimeRow> findAvailableTimesById(Long themeId, LocalDate localDate);

    public List<ThemeRow> findPopulars(int limit, int days, LocalDate date);
}
