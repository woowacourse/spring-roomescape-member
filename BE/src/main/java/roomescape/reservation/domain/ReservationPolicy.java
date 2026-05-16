package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.reservationTime.domain.ReservationTime;

public interface ReservationPolicy {
    void validateCreatableDateTime(LocalDate date, ReservationTime time);
    void validateModifiableDateTime(LocalDate date, ReservationTime time);
}
