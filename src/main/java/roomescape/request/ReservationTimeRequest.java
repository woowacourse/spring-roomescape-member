package roomescape.request;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeRequest(LocalTime startAt) {
    public ReservationTime toDomainForSave() {
        return new ReservationTime(null, startAt);
    }
}
