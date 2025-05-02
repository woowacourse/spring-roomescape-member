package roomescape.reservation.repository;

import roomescape.reservation.entity.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean deleteById(Long id);

    List<Reservation> findAllByTimeId(Long id);
}
