package roomescape.repository;

import roomescape.model.ReservationTime;

import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeRepository {
    List<ReservationTime> findAllReservationTimes();

    ReservationTime findReservationById(long id);

    ReservationTime addReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(long id);

    Long countReservationTimeById(long id);

    Long countReservationTimeByStartAt(LocalTime startAt);
}
