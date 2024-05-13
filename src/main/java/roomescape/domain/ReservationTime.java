package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public boolean hasSameStartAt(ReservationTime other) {
        return this.startAt.equals(other.startAt);
    }

    public Long getId() {
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
        return Objects.equals(id, time.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
