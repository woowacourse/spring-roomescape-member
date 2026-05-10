package roomescape.controller.dto;

import java.time.LocalTime;

public class ReservationTimeCreateRequest {
    private final LocalTime startAt;

    public ReservationTimeCreateRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
