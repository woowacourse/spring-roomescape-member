package roomescape.repository.reservationTime;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTime.ReservationTimeCommand;
import roomescape.domain.ReservationTime.ReservationTimeCondition;
import roomescape.domain.ReservationTime.ReservationTimeWithAvailable;

public interface ReservationTimeRepository {
    ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand);
    Optional<ReservationTime> getReservationTime(long id);
    List<ReservationTime> getAllReservationTime();
    void deleteReservationTime(long id);
    List<ReservationTimeWithAvailable> getReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition);
}
