package roomescape.domain.Reservation;

import roomescape.domain.ReservationTime.ReservationTime;

public record Reservation(long id, String name, String date, ReservationTime time) {
}
