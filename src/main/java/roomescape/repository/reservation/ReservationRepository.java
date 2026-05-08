package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationCommand;
import roomescape.domain.theme.Theme;
import roomescape.domain.reservationTime.ReservationTime;

public interface ReservationRepository {
    List<Reservation> getAllReservation();
    List<Reservation> getAllReservationByName(String name);
    Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
    boolean existsByTimeIdAndThemeIdAndDate(long timeId, long themeId, String date);
}
