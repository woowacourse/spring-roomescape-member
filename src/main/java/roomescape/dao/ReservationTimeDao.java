package roomescape.dao;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAllTimes();

    ReservationTime findTimeById(Long id);

    ReservationTime addTime(ReservationTime reservationTime);

    boolean existTimeByStartAt(LocalTime startAt);

    void removeTimeById(Long id);
}
