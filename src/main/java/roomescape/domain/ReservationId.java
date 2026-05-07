package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ReservationId(Long value) {

    public ReservationId {
        if (value == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_ID);
        }
    }
}
