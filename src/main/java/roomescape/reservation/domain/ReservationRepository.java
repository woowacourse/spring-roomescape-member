package roomescape.reservation.domain;

import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
    List<Reservation> findAll();
    boolean existsByReservationTime(Long timeId);
    boolean existsById(Long id);
    void deleteById(Long id);
}
