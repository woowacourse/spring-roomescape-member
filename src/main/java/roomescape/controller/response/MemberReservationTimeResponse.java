package roomescape.controller.response;

import java.time.LocalTime;

public class MemberReservationTimeResponse {

    private final long timeId;
    private final LocalTime startAt;
    private final boolean isBooked;

    public MemberReservationTimeResponse(long timeId, LocalTime startAt, boolean isBooked) {
        this.timeId = timeId;
        this.startAt = startAt;
        this.isBooked = isBooked;
    }

    public long getTimeId() {
        return timeId;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean getIsBooked() {
        return isBooked;
    }
}
