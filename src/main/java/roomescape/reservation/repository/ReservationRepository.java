package roomescape.reservation.repository;

import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findByIdOrThrow(Long id);

    Reservation add(Reservation reservation);

    void delete(Long id);
}
