package roomescape.domain;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    Reservation insertReservation(Reservation reservation);

    void deleteReservationById(long id);

    boolean hasReservationOf(long timeId);

    boolean isExistReservationOf(long id);
}
