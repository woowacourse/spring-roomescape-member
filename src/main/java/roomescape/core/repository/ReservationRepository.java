package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Reservation;

public interface ReservationRepository {
    Long save(final Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findByTimeId(final long timeId);

    void deleteById(final long id);
}
