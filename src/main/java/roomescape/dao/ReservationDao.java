package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAllReservations();

    Reservation addReservation(Reservation reservation);

    void removeReservationById(Long id);
}
