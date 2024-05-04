package roomescape.domain;

import java.time.LocalTime;

import roomescape.domain.exception.Validate;

public record ReservationTime(Long id, LocalTime startAt) {
    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime {
        Validate.AllNonNull(startAt);
    }

    public ReservationTime createWithId(Long id) {
        return new ReservationTime(id, startAt);
    }

    public boolean isBefore(LocalTime currentTime) {
        return startAt.isBefore(currentTime);
    }
}
