package roomescape.reservations.entity;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    Optional<Reservation> findById(Long id);
    List<Reservation> findAll();
    boolean existsByReservationTimeId(Long id);
    void deleteById(Long id);
}
