package roomescape.domain.time.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.time.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findAll();

    int deleteById(long id);
}
