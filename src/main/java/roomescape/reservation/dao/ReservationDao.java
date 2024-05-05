package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

public interface ReservationDao {

    Reservation save(Reservation reservation);

    List<Reservation> findAllReservationOrderByDateAndTimeStartAt();

    List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date);

    List<Theme> findThemeByDateOrderByThemeIdCountLimit(LocalDate startDate, LocalDate endDate, int limitCount);

    Optional<Reservation> findByTimeId(long timeId);

    void deleteById(long reservationId);
}
