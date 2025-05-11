package roomescape.dao.reservationtime;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationtime.ReservationTime;

public interface ReservationTimeDao {

    List<ReservationTime> findAllReservationTimes();

    void saveReservationTime(ReservationTime reservationTime);

    void deleteReservationTime(Long id);

    Optional<ReservationTime> findById(Long id);
}
