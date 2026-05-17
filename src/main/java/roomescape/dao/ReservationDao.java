package roomescape.dao;


import roomescape.dao.row.ReservationRow;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao extends CommonDao<ReservationRow> {
    boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date);

    boolean existsByThemeIdAndTimeIdAndDateAndIdNot(Long themeId, Long timeId, LocalDate date, Long id);

    boolean existsByThemeId(Long themeId);

    boolean existsByTimeId(Long timeId);

    List<ReservationRow> findByName(String name);

    ReservationRow update(ReservationRow row);
}
