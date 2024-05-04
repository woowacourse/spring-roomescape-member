package roomescape.controller.time.dto;

import roomescape.domain.ReservationTime;

public record CreateTimeRequest(String startAt) {

    public ReservationTime toDomain() {
        return new ReservationTime(startAt);
    }
}
