package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalTime;

public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;

    public ReservationTime(LocalTime startAt) {
        this(null, startAt);
    }

    public ReservationTime(Long id, LocalTime startAt) {
        validateStartAt(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public ReservationTime withId(Long id) {
        validateId(id);

        if (this.id != null) {
            throw new DomainException(ErrorCode.RESERVATION_TIME_ALREADY_HAS_ID);
        }

        return new ReservationTime(id, startAt);
    }

    private void validateStartAt(LocalTime startAt) {
        if (startAt == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME_ID);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
