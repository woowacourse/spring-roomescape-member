package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.model.Reservation;

public interface ReservationRepository {
    Long save(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    void deleteById(Long id);
}
