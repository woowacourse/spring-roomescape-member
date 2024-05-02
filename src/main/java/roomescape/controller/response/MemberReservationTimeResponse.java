package roomescape.controller.response;

import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberReservationTimeResponse that = (MemberReservationTimeResponse) o;
        return timeId == that.timeId && alreadyBooked == that.alreadyBooked && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeId, startAt, alreadyBooked);
    }
}
