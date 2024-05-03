package roomescape.controller.time.dto;

import roomescape.domain.ReservationTime;

public record TimeRequest(String startAt) {

    public ReservationTime toDomain() {
        return new ReservationTime(startAt);
    }
}
