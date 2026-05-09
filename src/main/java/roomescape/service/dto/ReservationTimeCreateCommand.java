package roomescape.service.dto;

import java.time.LocalTime;

public class ReservationTimeCreateCommand {
    private final LocalTime startAt;

    public ReservationTimeCreateCommand(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
