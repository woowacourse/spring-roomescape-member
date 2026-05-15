package roomescape.dao;


import roomescape.dao.row.ReservationRow;

import java.time.LocalDate;
import java.util.Optional;

public interface ReservationDao extends CommonDao<ReservationRow> {
    boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date);

    boolean existsByThemeId(Long themeId);

    boolean existsByTimeId(Long timeId);

    Optional<ReservationRow> findByName(String name);

    ReservationRow update(ReservationRow row);
}
