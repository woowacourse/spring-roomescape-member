package roomescape.controller.time;

import roomescape.domain.ReservationTime;

public record TimeRequest(String startAt) {

    public ReservationTime toDomain() {
        return ReservationTime.from(startAt);
    }
}
