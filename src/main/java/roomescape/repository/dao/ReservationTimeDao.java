package roomescape.repository.dao;

import roomescape.model.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeDao {

    List<ReservationTime> findAllReservationTimes();

    ReservationTime findReservationTimeById(long id);

    ReservationTime saveReservationTime(ReservationTime reservationTime);

    void deleteReservationTimeById(long id);

    Long countReservationTimeById(long id);

    Long countReservationTimeByStartAt(LocalTime startAt);
}
