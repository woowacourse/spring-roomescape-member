package roomescape.domain;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime create(ReservationTime reservationTime);

    void removeById(Long id);
}
