package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

public record ReservationName(String value) {

    public ReservationName {
        if (value == null || value.isBlank()) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_NAME);
        }
    }
}
