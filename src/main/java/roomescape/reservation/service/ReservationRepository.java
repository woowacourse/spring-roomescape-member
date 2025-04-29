package roomescape.reservation.service;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    Optional<Reservation> findById(Long id);

    void deleteById(Long id);

    boolean existSameDateTime(ReservationDate reservationDate, Long timeId);

    boolean existReservationByTimeId(Long timeId);
}
