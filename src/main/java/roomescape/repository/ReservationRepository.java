package roomescape.repository;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> readAll();

    void delete(Long id);

    boolean existsByTimeId(Long id);
}
