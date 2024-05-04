package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

import roomescape.domain.exception.Validate;

public class ReservationTime {
    private final Validate validate = new Validate();
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        validate.AllNonNull(startAt);
        this.id = null;
        this.startAt = startAt;
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validate.AllNonNull(id, startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime createWithId(Long id) {
        return new ReservationTime(id, this.startAt);
    }

    public boolean isBefore(LocalTime currentTime) {
        return startAt.isBefore(currentTime);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime time = (ReservationTime) o;
        return Objects.equals(id, time.id) && Objects.equals(startAt, time.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
