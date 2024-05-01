package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationCreateRequest(String name, String date, long timeId) {
    public Reservation createReservation(ReservationTime time) {
        return new Reservation(name, date, time);
    }
}
