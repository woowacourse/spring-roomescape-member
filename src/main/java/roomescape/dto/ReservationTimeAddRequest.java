package roomescape.dto;

import java.time.LocalTime;

public class ReservationTimeAddRequest {
    private LocalTime startAt;

    public ReservationTimeAddRequest() {

    }

    public ReservationTimeAddRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
