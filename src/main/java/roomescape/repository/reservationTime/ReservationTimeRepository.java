package roomescape.repository.reservationTime;

import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTime addReservationTime(ReservationTime reservationTime);
    Optional<ReservationTime> getReservationTime(long id);
    List<ReservationTime> getAllReservationTime();
    void deleteReservationTime(long id);
    List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition);
}
