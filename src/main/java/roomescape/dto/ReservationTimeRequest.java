package roomescape.dto;

import java.time.LocalTime;

public class ReservationTimeRequest {
    private final LocalTime startAt;

    public ReservationTimeRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
