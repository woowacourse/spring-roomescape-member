package roomescape.model;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTime {

    private long id;
    private LocalTime startAt;

    private ReservationTime() {
    }

    public ReservationTime(LocalTime startAt) {
        this.startAt = startAt;
    }

    public ReservationTime(long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
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
        ReservationTime that = (ReservationTime) o;
        return id == that.id && Objects.equals(startAt, that.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
