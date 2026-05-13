package roomescape.repository.reservationTime;

import java.util.List;
import java.util.Optional;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCommand;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;

public interface ReservationTimeRepository {
    ReservationTime addReservationTime(ReservationTimeCommand reservationTimeCommand);
    Optional<ReservationTime> getReservationTime(long id);
    List<ReservationTime> getAllReservationTime();
    void deleteReservationTime(long id);
    List<ReservationTimeWithAvailable> getAvailableReservationTimeByDateAndTheme(ReservationTimeCondition reservationTimeCondition);
    boolean isExistsById(long id);
}
