package roomescape.domain;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    Reservation insertReservation(Reservation reservation);

    void deleteReservationById(long id);
}
