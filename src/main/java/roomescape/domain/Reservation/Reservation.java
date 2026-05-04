package roomescape.domain.Reservation;

import roomescape.domain.ReservationTime.ReservationTime;
import roomescape.domain.Theme.Theme;

public record Reservation(long id, String name, String date, ReservationTime time, Theme theme) {
}
