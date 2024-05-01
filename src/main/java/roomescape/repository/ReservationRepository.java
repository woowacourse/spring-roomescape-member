package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public interface ReservationRepository {
    List<Reservation> getAllReservations();

    Reservation addReservation(Reservation reservation);

    long deleteReservation(long id);

    Long countReservationById(long id);

    Long countReservationByTimeId(long timeId);

    Long countReservationByDateAndTimeId(LocalDate date, long timeId);
}
