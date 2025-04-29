package roomescape.repository;

import java.time.LocalTime;
import java.util.List;
import roomescape.model.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime addTime(LocalTime startAt);

    List<ReservationTime> getAllTime();

    void deleteTime(Long id);

    ReservationTime getReservationTimeById(Long id);
}
