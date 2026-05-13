package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime addTime(ReservationTime reservationTime);

    List<ReservationTime> findAllReservationTimes();

    void deleteTime(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existsByTimeId(Long timeId);
}
