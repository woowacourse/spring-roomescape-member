package roomescape.reservation.dto;

import java.time.LocalTime;

public class ReservationTimeRequest {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTimeRequest(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
