package roomescape.domain;

import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;

import java.time.LocalTime;

public record ReservationTimeStartAt(LocalTime value) {

    public ReservationTimeStartAt {
        if (value == null) {
            throw new DomainException(ErrorCode.INVALID_RESERVATION_TIME);
        }
    }
}
