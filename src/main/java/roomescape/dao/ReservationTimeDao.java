package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAllReservationTimes();

    long saveReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(Long id);

    Optional<ReservationTime> findById(Long id);
}
