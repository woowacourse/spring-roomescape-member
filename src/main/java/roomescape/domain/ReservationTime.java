package roomescape.domain;

import lombok.Getter;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalTime;
import java.util.Objects;

@Getter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationTime that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
