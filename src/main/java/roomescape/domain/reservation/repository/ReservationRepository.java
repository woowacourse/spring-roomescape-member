package roomescape.domain.reservation.repository;

import roomescape.domain.reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findByUsername(String username);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean exists(Reservation reservation);
}
