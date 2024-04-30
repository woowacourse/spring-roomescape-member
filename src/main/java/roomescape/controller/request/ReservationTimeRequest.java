package roomescape.controller.request;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.Objects;

public record ReservationTimeRequest(LocalTime startAt) {
    public ReservationTimeRequest {
        Objects.requireNonNull(startAt);
    }

    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
