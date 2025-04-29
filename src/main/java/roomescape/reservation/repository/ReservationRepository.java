package roomescape.reservation.repository;

import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> getAll();

    Reservation put(Reservation reservation);

    boolean deleteById(Long id);
}
