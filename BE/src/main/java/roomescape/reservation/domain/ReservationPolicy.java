package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.reservationTime.domain.ReservationTime;

public interface ReservationPolicy {
    void pastDateTime(LocalDate date, ReservationTime time);
}
