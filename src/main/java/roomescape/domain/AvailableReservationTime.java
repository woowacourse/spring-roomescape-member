package roomescape.domain;

import java.time.LocalTime;

public class AvailableReservationTime {

    private final Long timeId;
    private final LocalTime startAt;
    private final boolean alreadyBooked;

    public AvailableReservationTime(Long timeId, LocalTime startAt, boolean alreadyBooked) {
        this.timeId = timeId;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public Long getTimeId() {
        return timeId;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean isAlreadyBooked() {
        return alreadyBooked;
    }
}
