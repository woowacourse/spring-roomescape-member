package roomescape.domain.Reservation;

import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.ReservationTheme.ReservationTheme;

public record Reservation(long id, String name, String date, ReservationTime time, ReservationTheme reservationTheme) {
}
