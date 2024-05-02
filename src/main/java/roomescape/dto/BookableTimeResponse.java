package roomescape.dto;

import java.time.LocalTime;

public class BookableTimeResponse {

    private final LocalTime startAt;
    private final Long timeId;
    private final boolean alreadyBooked;

    public BookableTimeResponse(LocalTime startAt, Long timeId, boolean alreadyBooked) {
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
