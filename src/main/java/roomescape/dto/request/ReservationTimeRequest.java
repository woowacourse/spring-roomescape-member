package roomescape.dto.request;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(
        LocalTime startAt
) {
    public ReservationTime toEntity() {
        return new ReservationTime(startAt);
    }
}
