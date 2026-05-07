package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ReservationTimeId(Long value) {

    public ReservationTimeId {
        if (value == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME_ID);
        }
    }
}
