package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public interface ReservationRepository {

    List<Reservation> getAllReservations();

    Reservation addReservation(Reservation reservation);

    void deleteReservation(Long id);

    boolean contains(LocalDate reservationDate, Long timeId);
}
