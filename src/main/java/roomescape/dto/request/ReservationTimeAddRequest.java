package roomescape.dto.request;

import java.time.LocalTime;

import jakarta.annotation.Nullable;
import roomescape.domain.ReservationTime;

public record ReservationTimeAddRequest(LocalTime startAt) {

    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
