package roomescape.controller.response;

import java.time.LocalTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberReservationTimeResponse that = (MemberReservationTimeResponse) o;
        return timeId == that.timeId && isBooked == that.isBooked && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeId, startAt, isBooked);
    }
}
