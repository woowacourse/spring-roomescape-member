package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.ReservationTime;

public interface ReservationTimeRepository {
    Long save(final ReservationTime reservationTime);

    List<ReservationTime> findAll();

    ReservationTime findById(final long id);

    void deleteById(final long id);
}
