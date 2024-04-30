package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findAll();

    void delete(long id);
}
