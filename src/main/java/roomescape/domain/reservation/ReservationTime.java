package roomescape.domain.reservation;

import roomescape.domain.exception.InvalidDomainObjectException;

import java.time.LocalTime;
import java.util.Objects;

import static java.util.Objects.isNull;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validate(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    private void validate(LocalTime startAt) {
        if (isNull(startAt)) {
            throw new InvalidDomainObjectException("startAt must not be null");
        }
    }

    public boolean hasSameStartAt(ReservationTime reservationTime) {
        return this.startAt.equals(reservationTime.startAt);
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
