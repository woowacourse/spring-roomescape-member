package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(0L, startAt);
    }

    public ReservationTime(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public boolean hasSameStartAt(ReservationTime reservationTime) {
        return this.startAt.equals(reservationTime.startAt);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTime time = (ReservationTime) o;
        return id == time.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
