package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTime from(final String startAt) {
        return new ReservationTime(null, LocalTime.parse(startAt));
    }

public record ReservationTime(Long id, LocalTime startAt) {

    public ReservationTime assignId(Long id) {
        return new ReservationTime(id, startAt);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ReservationTime) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }

    @Override
    public String toString() {
        return "ReservationTime[" +
                "id=" + id + ", " +
                "startAt=" + startAt + ']';
    }
}
