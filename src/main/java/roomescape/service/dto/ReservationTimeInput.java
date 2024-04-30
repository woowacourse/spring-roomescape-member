package roomescape.service.dto;

import roomescape.domain.ReservationTime;

public record ReservationTimeInput(String startAt) {

    public ReservationTime toReservationTime() {
        return ReservationTime.from(null, startAt);
    }
}
