package roomescape.dto;

import roomescape.domain.ReservationTime;

public record ReservationTimeRequest(String startAt) {
    public ReservationTime toReservationTime() {
        return new ReservationTime(startAt);
    }
}
