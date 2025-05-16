package roomescape.reservation.repository;

import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.dto.ReservationWithFilterRequest;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Reservation save(Reservation entity);

    boolean deleteById(Long id);

    List<Reservation> findAll();

    List<Reservation> findAllByFilter(ReservationWithFilterRequest filterRequest);

    List<Reservation> findAllByTimeId(Long id);

    Optional<Reservation> findDuplicatedWith(Reservation entity);
}
