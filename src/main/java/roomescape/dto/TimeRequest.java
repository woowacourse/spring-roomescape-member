package roomescape.dto;

import java.time.LocalTime;

public class TimeRequest {
    private LocalTime startAt;

    public TimeRequest(LocalTime startAt) {
        this.startAt = startAt;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
