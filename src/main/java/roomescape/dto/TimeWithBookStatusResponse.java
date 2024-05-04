package roomescape.dto;

import java.time.LocalTime;

public class TimeWithBookStatusResponse {

    private final Long timeId;
    private final LocalTime startAt;
    private final boolean alreadyBooked;

    public TimeWithBookStatusResponse(Long timeId, LocalTime startAt, boolean alreadyBooked) {
        this.startAt = startAt;
        this.timeId = timeId;
        this.alreadyBooked = alreadyBooked;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public Long getTimeId() {
        return timeId;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
