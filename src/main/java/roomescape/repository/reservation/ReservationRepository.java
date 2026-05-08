package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.Theme.Theme;
import roomescape.domain.ReservationTime.ReservationTime;

public interface ReservationRepository {
    List<Reservation> getAllReservation();
    List<Reservation> getAllReservationByName(String name);
    Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, String date);
}
