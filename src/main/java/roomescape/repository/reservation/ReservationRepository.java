package roomescape.repository.reservation;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation createReservation(Reservation reservation);

    void deleteById(long id);

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    boolean existsByTimeId(long timeId);
}
