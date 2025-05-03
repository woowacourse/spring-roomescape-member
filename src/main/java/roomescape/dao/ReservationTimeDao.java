package roomescape.dao;

import java.util.List;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAllReservationTimes();

    long saveReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(Long id);

    ReservationTime findById(Long id);
}
