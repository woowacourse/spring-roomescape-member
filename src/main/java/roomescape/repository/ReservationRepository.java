package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();

    Optional<Reservation> findById(Long reservationId);

    Reservation save(Reservation reservation);

    void deleteById(Long reservationId);

    boolean existByDateAndTimeId(LocalDate date, Long timeId);

    boolean existByTimeId(Long reservationTimeId);
}
