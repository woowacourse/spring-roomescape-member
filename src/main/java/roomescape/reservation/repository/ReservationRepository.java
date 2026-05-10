package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.entity.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    int deleteById(Long id);

}
