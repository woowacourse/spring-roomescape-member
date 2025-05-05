package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.model.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime addTime(ReservationTime time);

    List<ReservationTime> getAllTime();

    int deleteTime(Long id);

    Optional<ReservationTime> findById(Long id);
}
