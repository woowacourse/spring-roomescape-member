package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;

public interface ReservationRepository {
    List<Reservation> getAllReservation();
    Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime, Theme theme);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
    boolean existsByThemeId(long themeId);
}
