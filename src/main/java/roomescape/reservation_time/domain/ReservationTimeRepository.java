package roomescape.reservation_time.domain;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    boolean existsById(ReservationTimeId id);

    Optional<ReservationTime> findById(ReservationTimeId id);

    List<ReservationTime> findAll();

    ReservationTime save(ReservationTime reservationTime);

    void deleteById(ReservationTimeId id);
}
