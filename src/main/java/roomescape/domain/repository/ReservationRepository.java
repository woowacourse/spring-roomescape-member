package roomescape.domain.repository;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Long save(Reservation reservation);

    boolean deleteById(Long id);
}
