package roomescape.reservationtime.dto.request;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;

public record CreateReservationTimeRequest(LocalTime startAt) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(
                null,
                startAt);
    }
}
