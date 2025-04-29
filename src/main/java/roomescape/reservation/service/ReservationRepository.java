package roomescape.reservation.service;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    void deleteById(Long id);
}
