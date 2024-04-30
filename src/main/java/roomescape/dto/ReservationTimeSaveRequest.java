package roomescape.dto;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record ReservationTimeSaveRequest(LocalTime startAt) {

    public ReservationTime toModel() {
        return new ReservationTime(startAt);
    }
}
