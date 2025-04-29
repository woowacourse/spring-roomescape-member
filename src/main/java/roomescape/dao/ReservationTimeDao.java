package roomescape.dao;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAllTimes();

    ReservationTime findTimeById(Long id);

    ReservationTime addTime(ReservationTime reservationTime);

    void removeTimeById(Long id);
}
