package roomescape.reservation.presentation.dto;

import java.time.LocalTime;

public class ReservationTimeRequest {
    private final LocalTime startAt;

    public ReservationTimeRequest(final LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
