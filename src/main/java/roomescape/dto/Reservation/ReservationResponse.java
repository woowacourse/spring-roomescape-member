package roomescape.dto.Reservation;

import roomescape.domain.Reservation.Reservation;
import roomescape.dto.ReservationTime.ReservationTimeResponse;

public record ReservationResponse(long id, String name, String date, ReservationTimeResponse time) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.id(), reservation.name(), reservation.date(), ReservationTimeResponse.from(reservation.time()));
    }
}
