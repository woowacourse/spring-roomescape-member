package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    boolean hasSameReservation(Reservation reservation);

    void delete(Long id);

    List<Reservation> findAll();
}
