package roomescape.reservationtime.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import roomescape.reservationtime.ReservationTime;

public record ReservationTimeRequest(
        @NotNull LocalTime startAt
) {
    public ReservationTime toTime() {
        return ReservationTime.createWithoutId(startAt);
    }
}
