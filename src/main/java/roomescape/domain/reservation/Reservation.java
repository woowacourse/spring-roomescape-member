package roomescape.domain.reservation;

import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTheme.ReservationTheme;

public record Reservation(long id, String name, String date, ReservationTime time, ReservationTheme reservationTheme) {
}
