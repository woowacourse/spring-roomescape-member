package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTheme.ReservationTheme;

public interface ReservationRepository {
    List<Reservation> getAllReservation();
    List<Reservation> getAllReservationByName(String name);
    Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, ReservationTheme theme);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(ReservationCommand reservationCommand);
}
