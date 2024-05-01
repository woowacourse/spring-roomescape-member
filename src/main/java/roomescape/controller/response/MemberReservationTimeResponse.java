package roomescape.controller.response;

import java.time.LocalTime;

public class MemberReservationTimeResponse {

    private final long timeId;
    private final LocalTime startAt;
    private boolean alreadyBooked;

    public MemberReservationTimeResponse(long timeId, LocalTime startAt, boolean alreadyBooked) {
        this.timeId = timeId;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public void setAlreadyBooked(boolean alreadyBooked) {
        this.alreadyBooked = alreadyBooked;
    }

    public long getTimeId() {
        return timeId;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean getAlreadyBooked() {
        return alreadyBooked;
    }
}
