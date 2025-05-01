package roomescape.repository;

import java.util.List;
import roomescape.model.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime addTime(ReservationTime time);

    List<ReservationTime> getAllTime();

    void deleteTime(Long id);

    ReservationTime findById(Long id);
}
