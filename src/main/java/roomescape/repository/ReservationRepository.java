package roomescape.repository;

import roomescape.domain.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAllReservations();

    Reservation addReservation(Reservation reservation);

    void deleteById(Long id);
}
