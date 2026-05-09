package roomescape.dao;


import roomescape.dao.row.ReservationRow;

import java.time.LocalDate;

public interface ReservationDao extends CommonDao<ReservationRow> {
    boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date);

    boolean existsByThemeId(Long themeId);

    boolean existsByTimeId(Long timeId);
}
