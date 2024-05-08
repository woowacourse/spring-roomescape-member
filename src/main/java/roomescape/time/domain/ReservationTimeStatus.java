package roomescape.time.domain;

import java.time.LocalTime;
import java.util.Objects;

public class ReservationTimeStatus {
    private final Long id;
    private final LocalTime startAt;
    private final Boolean alreadyBooked;

    public ReservationTimeStatus(Long id, LocalTime startAt, Boolean alreadyBooked) {
        this.id = id;
        this.startAt = startAt;
        this.alreadyBooked = alreadyBooked;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    public Boolean getAlreadyBooked() {
        return alreadyBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTimeStatus that = (ReservationTimeStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(startAt, that.startAt) && Objects.equals(alreadyBooked, that.alreadyBooked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt, alreadyBooked);
    }

    @Override
    public String toString() {
        return "ReservationTimeStatus{" +
                "id=" + id +
                ", startAt=" + startAt +
                ", alreadyBooked=" + alreadyBooked +
                '}';
    }
}
