package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation createReservation(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findAll();

    Reservation findById(Long id);

    boolean existsByTimeId(Long timeId);
}
