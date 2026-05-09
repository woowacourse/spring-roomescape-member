package roomescape.domain.reservation;

import roomescape.domain.theme.Theme;
import roomescape.domain.reservationTime.ReservationTime;

public record Reservation(long id, String name, String date, ReservationTime time, Theme theme) {
}
