package roomescape.dto;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationCreateRequest(String name, String date, long timeId) {
    public Reservation createReservation() {
        return new Reservation(null, name, date, new ReservationTime(timeId, null));
    }
}
