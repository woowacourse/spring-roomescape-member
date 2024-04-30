package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean deleteById(long reservationId);

    List<Reservation> findAllByTimeId(long timeId);

    boolean existsByDateTime(LocalDate date, long timeId);
}
