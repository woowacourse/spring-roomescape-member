package roomescape.global.exception.policy;

import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.ReservationException;

public class ReservationConflictException extends ReservationException {

    public ReservationConflictException() {
        super(ErrorCode.RESERVATION_DUPLICATED, ErrorCode.RESERVATION_DUPLICATED.getMessage());
    }
}
