package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAllReservationOrderByDateAndTimeStartAt();

    List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date);

    List<Theme> findThemeByDateOrderByThemeIdCountLimit(LocalDate startDate, LocalDate endDate, int limitCount);

    void deleteById(long reservationId);

    int countByTimeId(long timeId);
}
