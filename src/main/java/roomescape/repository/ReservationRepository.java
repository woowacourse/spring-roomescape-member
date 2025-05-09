package roomescape.repository;

import roomescape.domain.Reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(Long id);

    boolean existsByTimeId(Long id);
}
