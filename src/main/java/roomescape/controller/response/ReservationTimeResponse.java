package roomescape.controller.response;

import roomescape.model.ReservationTime;

import java.time.LocalTime;

public class ReservationTimeResponse {

    private final long id;
    private final LocalTime startAt;

    public ReservationTimeResponse(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
