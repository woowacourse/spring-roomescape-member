package roomescape.service.dto.input;

import roomescape.domain.ReservationTime;

public record ReservationTimeInput(String startAt) {

    public ReservationTime toReservationTime() {
        return ReservationTime.from(null, startAt);
    }
}
