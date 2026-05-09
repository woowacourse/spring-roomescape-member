package roomescape.time.repository;

import java.util.List;
import java.util.Optional;
import roomescape.time.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    void deleteById(long id);
}
