package roomescape.domain;

import java.util.List;

public interface ReservationTimeRepository {

    List<ReservationTime> findAllReservationTimes();

    ReservationTime insertReservationTime(ReservationTime reservationTime);

    void deleteReservationTimeById(long id);
}
