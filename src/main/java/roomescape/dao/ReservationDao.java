package roomescape.dao;


import java.time.LocalDate;
import roomescape.domain.Reservation;

public interface ReservationDao extends CommonDao<Reservation> {
    boolean existsByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date);

    boolean existsByThemeId(Long themeId);

    boolean existsByTimeId(Long timeId);
}
