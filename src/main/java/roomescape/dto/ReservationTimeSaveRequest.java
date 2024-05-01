package roomescape.dto;

import roomescape.domain.ReservationTime;

public record ReservationTimeSaveRequest(String startAt) {

    public ReservationTime toModel() {
        return new ReservationTime(startAt);
    }
}
