package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation.Reservation;
import roomescape.domain.Reservation.ReservationCommand;
import roomescape.domain.ReservationTime.ReservationTime;

public interface ReservationRepository {
    List<Reservation> getAllReservation();
    Reservation addReservation(ReservationCommand reservationCommand, ReservationTime reservationTime);
    void deleteReservation(long id);
    boolean existsByTimeId(long timeId);
}
