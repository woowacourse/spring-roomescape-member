package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    boolean existsByReservationDateTime(LocalDate date, long timeId);

    Optional<Reservation> findById(long id);

    List<Reservation> findAll();

    void deleteById(long id);
}
