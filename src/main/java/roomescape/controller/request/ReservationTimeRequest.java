package roomescape.controller.request;

import java.time.LocalTime;

public class ReservationTimeRequest {

    private LocalTime startAt;

    public ReservationTimeRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    private ReservationTimeRequest() {
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
