package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import roomescape.reservation.entity.Reservation;

public interface ReservationRepository {

    long generateId();

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    Reservation save(Reservation reservation);

    void deleteById(long id);
}
