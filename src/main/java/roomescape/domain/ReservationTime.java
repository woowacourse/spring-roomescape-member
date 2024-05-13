package roomescape.domain;

import java.time.LocalTime;
import java.util.Objects;

import roomescape.domain.util.Validator;

public record ReservationTime(Long id, LocalTime startAt) {
    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime {
        Validator.nonNull(startAt);
    }

    public ReservationTime createWithId(Long id) {
        return new ReservationTime(id, startAt);
    }

    public boolean isBefore(LocalTime currentTime) {
        return startAt.isBefore(currentTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationTime time = (ReservationTime) o;
        if (id == null || time.id == null)
            return Objects.equals(startAt, time.startAt);
        return Objects.equals(id, time.id);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return Objects.hash(startAt);
        return Objects.hash(id);
    }
}
