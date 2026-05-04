package roomescape.repository.reservation;

import roomescape.domain.Reservation;
import java.util.List;

public interface ReservationRepository {

    Long createReservation(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findAll();

    Reservation findById(Long id);
}
