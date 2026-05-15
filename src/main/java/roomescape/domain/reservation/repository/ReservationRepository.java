package roomescape.domain.reservation.repository;

import roomescape.domain.reservation.entity.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findByUsername(String username);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean exists(Reservation reservation);
}
