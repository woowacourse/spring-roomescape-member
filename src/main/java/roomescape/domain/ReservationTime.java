package roomescape.domain;

import java.time.LocalTime;

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
}
